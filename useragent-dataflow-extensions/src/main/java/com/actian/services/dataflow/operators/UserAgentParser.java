package com.actian.services.dataflow.operators;

/*
		Copyright 2015 Actian Corporation

		Licensed under the Apache License, Version 2.0 (the "License");
		you may not use this file except in compliance with the License.
		You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

		Unless required by applicable law or agreed to in writing, software
		distributed under the License is distributed on an "AS IS" BASIS,
		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		See the License for the specific language governing permissions and
		limitations under the License.
*/


import static com.pervasive.datarush.io.WriteMode.OVERWRITE;
import static com.pervasive.datarush.types.TokenTypeConstant.*;
import static com.pervasive.datarush.types.TypeUtil.mergeTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.pervasive.datarush.annotations.PortDescription;
import com.pervasive.datarush.annotations.PropertyDescription;

import com.pervasive.datarush.operators.*;
import com.pervasive.datarush.ports.physical.*;
import com.pervasive.datarush.ports.record.*;
import com.pervasive.datarush.tokens.TokenUtils;
import com.pervasive.datarush.tokens.scalar.*;
import com.pervasive.datarush.types.RecordTokenType;
import com.pervasive.datarush.types.RecordTokenTypeBuilder;
import org.apache.commons.lang.BooleanUtils;

@JsonSerialize(include=Inclusion.NON_DEFAULT)
public class UserAgentParser extends ExecutableOperator implements RecordPipelineOperator {

	private final RecordPort input = newRecordInput("input");
	private final RecordPort output = newRecordOutput("output");
	private final RecordPort reject = newRecordOutput("reject");
	private String inputField;
	private String fieldPrefix;
    private HashMap<String,String> fieldNameMap;
    private RecordTokenTypeBuilder typeBuilder;
	private RecordOutput recordOutput;


	@PortDescription("Source records")
	public RecordPort getInput() {
		return input;
	}

	@PortDescription("Output records")
	public RecordPort getOutput() {
		return output;
	}

	@PortDescription("Rejected records")
	public RecordPort getReject() {
		return reject;
	}

	public String getInputField() {
		return inputField;
	}

	public void setInputField(String inputField) {
		this.inputField = inputField;
	}

	public String getFieldPrefix() { return fieldPrefix; }

	public void setFieldPrefix(String fieldPrefix) {
        if (fieldPrefix == null){
            this.fieldPrefix = "";
        }
        else {
            this.fieldPrefix = fieldPrefix;
        }
    }

	public UserAgentParser() {
	}

    protected void addName(String name) {
        fieldNameMap.put(name, getFieldPrefix() + name);
        typeBuilder.addField(field(STRING, getFieldPrefix() + name));
    }

    protected void createNameMap() {
        fieldNameMap = new HashMap<String, String>();

        // Setup the output schema for NewOperator
        typeBuilder = new RecordTokenTypeBuilder();

        // Build a new record layout with all of the prefixed user agent field names
        addName("name");
        addName("devicecategory_name");
        addName("devicecategory_icon");
        addName("devicecategory_infourl");
        addName("useragentfamily");
        addName("icon");
        addName("operatingsystem_name");
        addName("operatingsystem_icon");
        addName("operatingsystem_producer");
        addName("operatingsystem_producerurl");
        addName("operatingsystem_url");
        addName("operatingsystem_version");
        addName("producer");
        addName("producerurl");
        addName("typename");
        addName("url");
        addName("version");
    }

	@Override
	protected void computeMetadata(StreamingMetadataContext context) {
		//best practice: perform any input validation: should be done first
		// validateInput(context);

		//required: declare our parallelizability.
		//  in this case we use source parallelism as a hint for our parallelism.
		context.parallelize(ParallelismStrategy.NEGOTIATE_BASED_ON_SOURCE);

        // Create the name hash map and the layout for the user agent fields
        createNameMap();

        RecordTokenType outputType = mergeTypes(getInput().getType(context), typeBuilder.toType());
        getOutput().setType(context, outputType);

		RecordTokenType rejectType = mergeTypes(getInput().getType(context), record(STRING("ErrorText")));
        getReject().setType(context, rejectType);

		//best practice: define output ordering/distribution
		//  in this case we are generating data in a single field so
		//  the ordering is unspecified and the distribution is partial
		RecordMetadata outputMetadata = input.getCombinedMetadata(context);
		output.setOutputDataOrdering(context, DataOrdering.UNSPECIFIED);
		reject.setOutputDataOrdering(context, DataOrdering.UNSPECIFIED);
	}

	// Check the operator configuration
	private boolean checkConfig() {
		// Check the settings for NewOperator here.  Return false if there are configuration errors.
        if (inputField == null || inputField.isEmpty())
            return false;

        return true;
	}

	protected void setOutputField(String name, String value) {
		String outputFieldName = fieldNameMap.get(name);
		StringSettable outputField = (StringSettable) recordOutput.getField(outputFieldName);
		outputField.set(value);
	}


	@Override
	protected void execute(ExecutionContext context) {

		RecordInput recordInput = getInput().getInput(context);
		recordOutput = getOutput().getOutput(context);
		RecordOutput recordReject = getReject().getOutput(context);

		ScalarValued[] allInputs = recordInput.getFields();
		ScalarSettable[] outputs = TokenUtils.selectFields(recordOutput, recordInput.getType().getNames());
		ScalarSettable[] rejects = TokenUtils.selectFields(recordReject, recordInput.getType().getNames());

		// Quit early if the operator configuration isn't valid
		if (checkConfig() == false) {
			recordOutput.pushEndOfData();
			recordReject.pushEndOfData();
			return;
		}

        // Create the field name map
        createNameMap();

		// Get an UserAgentStringParser and analyze the requesting client
		UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();

		while (recordInput.stepNext()) {
			try {

				// Copy the original input record fields to the corresponding output record fields
				TokenUtils.transfer(allInputs, outputs);

				String userAgentSting = recordInput.getField(getInputField()).toString();
				ReadableUserAgent agent = parser.parse(userAgentSting);

				// Set the values for the user agent fields
				setOutputField("name", agent.getName());
				setOutputField("devicecategory_name", agent.getDeviceCategory().getName());
				setOutputField("devicecategory_icon", agent.getDeviceCategory().getIcon());
				setOutputField("devicecategory_infourl", agent.getDeviceCategory().getInfoUrl());
				setOutputField("useragentfamily", agent.getFamily().getName());
				setOutputField("icon", agent.getIcon());
				setOutputField("operatingsystem_name", agent.getOperatingSystem().getName());
				setOutputField("operatingsystem_icon", agent.getOperatingSystem().getIcon());
				setOutputField("operatingsystem_producer", agent.getOperatingSystem().getProducer());
				setOutputField("operatingsystem_producerurl", agent.getOperatingSystem().getProducerUrl());
				setOutputField("operatingsystem_url", agent.getOperatingSystem().getUrl());
				setOutputField("operatingsystem_version", agent.getOperatingSystem().getVersionNumber().toVersionString());
				setOutputField("producer", agent.getProducer());
				setOutputField("producerurl", agent.getProducerUrl());
				setOutputField("typename", agent.getTypeName());
				setOutputField("url", agent.getUrl());
				setOutputField("version", agent.getVersionNumber().toVersionString());

				recordOutput.push();
			}
			catch (Exception e)
			{
				// Copy the original input record fields to the corresponding reject record fields
				TokenUtils.transfer(allInputs, rejects);
				StringSettable errorField = (StringSettable) recordReject.getField("ErrorText");
				errorField.set(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
				recordReject.push();

			}
		}

		recordOutput.pushEndOfData();
		recordReject.pushEndOfData();
	}
}

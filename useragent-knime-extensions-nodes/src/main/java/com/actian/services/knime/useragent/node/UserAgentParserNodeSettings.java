package com.actian.services.knime.useragent.node;

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

import java.util.Arrays;
import java.util.List;

import com.actian.services.dataflow.operators.UserAgentParser;
import com.pervasive.datarush.knime.core.framework.AbstractDRSettingsModel;
import com.pervasive.datarush.knime.core.util.OptionalSettingsModelString;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.pervasive.datarush.ports.PortMetadata;

/*package*/ 
final class UserAgentParserNodeSettings extends AbstractDRSettingsModel<UserAgentParser> {

	public final SettingsModelString inputField = new SettingsModelString("inputField", null);
	public final OptionalSettingsModelString fieldPrefix = new OptionalSettingsModelString("fieldPrefix", "uad_");

	@Override
	protected List<SettingsModel> getComponentSettings() {
		return Arrays.<SettingsModel>
			asList(inputField, fieldPrefix);
	}

	@Override
	public void configure(PortMetadata[] inputTypes,
						  UserAgentParser operator) throws InvalidSettingsException {

		if (this.inputField.getStringValue() == null || this.inputField.getStringValue().trim().isEmpty()) {
			throw new InvalidSettingsException("Output String must not be empty!");
		}

		operator.setInputField(this.inputField.getStringValue());
		operator.setFieldPrefix(this.fieldPrefix.getStringValue());
	}

}

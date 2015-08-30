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
package com.actian.services.dataflow.functions.evaluators;

import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import com.pervasive.datarush.functions.FunctionEvaluator;
import com.pervasive.datarush.tokens.scalar.StringSettable;
import com.pervasive.datarush.tokens.scalar.StringValued;

public class UserAgentPartEvaluator implements FunctionEvaluator {

    private final StringSettable result;
    private final StringValued userAgentStr;
    private final StringValued userAgentPart;

    public UserAgentPartEvaluator(StringSettable result, StringValued userAgentStr, StringValued userAgentPart) {
        this.result = result;
        this.userAgentStr = userAgentStr;
        this.userAgentPart = userAgentPart;
    }

    @Override
    public void evaluate() {

        String output = "";
        try {

            // Get an UserAgentStringParser and analyze the requesting client
            UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
            ReadableUserAgent agent = parser.parse(userAgentStr.asString());

            String partName = userAgentPart.asString();
            output = "";

            // Set the values for the user agent fields
            if("name".equalsIgnoreCase(partName)) {
                output = agent.getName();
            }
            else if("devicecategory_name".equalsIgnoreCase(partName)) {
                output = agent.getDeviceCategory().getName();
            }
            else if("devicecategory_icon".equalsIgnoreCase(partName)) {
                output = agent.getDeviceCategory().getIcon();
            }
            else if("devicecategory_infourl".equalsIgnoreCase(partName)) {
                output = agent.getDeviceCategory().getInfoUrl();
            }
            else if("useragentfamily".equalsIgnoreCase(partName)) {
                output = agent.getFamily().getName();
            }
            else if("icon".equalsIgnoreCase(partName)) {
                output = agent.getIcon();
            }
            else if("operatingsystem_name".equalsIgnoreCase(partName)) {
                output = agent.getOperatingSystem().getName();
            }
            else if("operatingsystem_icon".equalsIgnoreCase(partName)) {
                output = agent.getOperatingSystem().getIcon();
            }
            else if("operatingsystem_producer".equalsIgnoreCase(partName)) {
                output = agent.getOperatingSystem().getProducer();
            }
            else if("operatingsystem_producerurl".equalsIgnoreCase(partName)) {
                output = agent.getOperatingSystem().getProducerUrl();
            }
            else if("operatingsystem_url".equalsIgnoreCase(partName)) {
                output = agent.getOperatingSystem().getUrl();
            }
            else if("operatingsystem_version".equalsIgnoreCase(partName)) {
                output = agent.getOperatingSystem().getVersionNumber().toVersionString();
            }
            else if("producer".equalsIgnoreCase(partName)) {
                output = agent.getProducer();
            }
            else if("producerurl".equalsIgnoreCase(partName)) {
                output = agent.getProducerUrl();
            }
            else if("typename".equalsIgnoreCase(partName)) {
                output = agent.getTypeName();
            }
            else if("url".equalsIgnoreCase(partName)) {
                output = agent.getUrl();
            }
            else if("version".equalsIgnoreCase(partName)) {
                output = agent.getVersionNumber().toVersionString();
            }
        } catch (Exception ex) {
            output = ex.getMessage();
        }

        result.set(output);
    }
}

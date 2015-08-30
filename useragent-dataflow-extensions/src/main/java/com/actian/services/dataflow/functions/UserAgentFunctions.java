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
package com.actian.services.dataflow.functions;

import static com.pervasive.datarush.functions.ScalarFunctionDescriptor.define;

import com.actian.services.dataflow.functions.evaluators.UserAgentPartEvaluator;
import com.pervasive.datarush.annotations.Function;
import com.pervasive.datarush.functions.ScalarValuedFunction;
import com.pervasive.datarush.types.TokenTypeConstant;

public class UserAgentFunctions {
    @Function(description="Extracts a specific part from a user_agent_str.  Valid part names are: name, devicecategory_name, devicecategory_icon, devicecategory_infourl, useragentfamily, icon, operatingsystem_name, operatingsystem_icon, operatingsystem_producer, operatingsystem_producerurl, operatingsystem_url, operatingsystem_version, producer, producerurl, typename, url, or version",
              category="UserAgent",
              argumentNames={"user_agent_str","part"})
    public static ScalarValuedFunction useragentpart(ScalarValuedFunction user_agent_str,
                                                     ScalarValuedFunction part) {
        return define("UserAgent.useragentpart", TokenTypeConstant.STRING, UserAgentPartEvaluator.class, user_agent_str, part);
    }
}

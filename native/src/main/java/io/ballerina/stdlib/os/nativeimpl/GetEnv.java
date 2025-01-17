/*
 * Copyright (c) 2023, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.ballerina.stdlib.os.nativeimpl;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.PredefinedTypes;
import io.ballerina.runtime.api.creators.TypeCreator;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.types.MapType;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;

import java.util.Map;

import static io.ballerina.stdlib.os.utils.OSConstants.ENV_VAR_KEY;

/**
 * Extern function of ballerina.os:getEnv.
 *
 * @since 1.5.1
 */
public class GetEnv {

    private GetEnv() {

    }

    public static BString getEnv(Environment env, BString key) {
        Object envVarMap = env.getStrandLocal(ENV_VAR_KEY);
        BMap<BString, Object> envMap;
        if (envVarMap == null) {
            MapType mapType = TypeCreator.createMapType(PredefinedTypes.TYPE_STRING);
            envMap = ValueCreator.createMapValue(mapType);
            Map<String, String> jEnvMap = System.getenv();
            for (Map.Entry<String, String> entry : jEnvMap.entrySet()) {
                envMap.put(StringUtils.fromString(entry.getKey()), StringUtils.fromString(entry.getValue()));
            }
            env.setStrandLocal(ENV_VAR_KEY, envMap);
        } else {
            envMap = (BMap<BString, Object>) envVarMap;
        }
        Object value = envMap.get(key);
        if (value == null) {
            return StringUtils.fromString("");
        }
        return (BString) value;
    }
}

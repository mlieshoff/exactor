/******************************************************************
 * Copyright (c) 2012, Exoftware
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *   * Neither the name of the Exoftware, Exactor nor the names
 *     of its contributors may be used to endorse or promote
 *     products derived from this software without specific
 *     prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************/
package com.exoftware.exactor.command.annotated;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Michael Lieshoff
 */
public class AnnotatedCommand extends Command {
    private Set<String> registeredParameterKeys = new HashSet<String>();
    private Map<Param, Field> registeredParameters = new HashMap<Param, Field>();
    private Map<String, NamedParameter> namedParameters = new HashMap<String, NamedParameter>();

    public AnnotatedCommand() {
        Class clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            for(Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof Param) {
                    Param param = (Param) annotation;
                    registeredParameters.put(param, field);
                    ParameterDefinition parameterDefinition = (ParameterDefinition) Enum.valueOf(param.namespace(),
                            param.name());
                    for(String s : parameterDefinition.getParameterNames()) {
                        registeredParameterKeys.add(s.toLowerCase());
                    }
                }
            }
        }
    }

    private void setUp() throws IllegalAccessException {
        for(Map.Entry<Param, Field> entry : registeredParameters.entrySet()) {
            Param param = entry.getKey();
            Field field = entry.getValue();
            field.setAccessible(true);
            ParameterDefinition parameterDefinition = (ParameterDefinition) Enum.valueOf(param.namespace(), param.name());
            for (String name : parameterDefinition.getParameterNames()) {
                if (getParameterByName(name) == null && param.settings() == ParameterType.MANDATORY) {
                    throw new IllegalArgumentException();
                }
            }
            Object value = parameterDefinition.resolve(param.settings(), this);
            if (value != null) {
                field.set(this, value);
            }
        }
    }

    public boolean isParameterRegistered(String parameterName) {
        return  registeredParameterKeys.contains(parameterName.toLowerCase());
    }

    public void addParameter(Parameter parameter) {
        String value = parameter.stringValue();
        String[] array = value.split("[=]");
        if (array.length == 2) {
            NamedParameter namedParameter = new NamedParameter(array[0], array[1]);
            super.addParameter(namedParameter);
            namedParameters.put(namedParameter.getName(), namedParameter);
        } else {
            super.addParameter(parameter);
        }
    }

    public Parameter getParameterByName(String parameterName) {
        return namedParameters.get(parameterName);
    }
}

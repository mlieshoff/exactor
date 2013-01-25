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
import com.exoftware.exactor.ValueType;
import com.sun.xml.internal.ws.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Base class for all annotated commands executed by the framework.
 * The class provides access to the named parameters of the command and the script which
 * contains the command.
 * The class extends <code>com.exoftware.exactor.Command</code> so all the assert methods
 * are available for use.
 * <p/>
 * Subclasses must implement the <code>execute</code> method and ensure a call of the method
 * <code>setUp</code> within.
 * If the command is responsible for performing a check then the <code>execute</code> method
 * should throw an <code>AssertionFailedError</code> in the event that the check was
 * unsuccessful, e.g.
 * <pre>
 * <code>
 *     public void execute() throws Exception {
 *         setUp();
 *         if( !"hello".equals( "goodbye" ) )
 *             throw new AssertionFailedError( "Values not equal" );
 *     }
 * </code>
 * </pre>
 * or using the convenience method,
 * <pre>
 * <code>
 *     public void execute() throws Exception {
 *         setUp();
 *         assertEquals( "hello", "goodbye" );
 *     }
 * </code>
 * </pre>
 *
 * In example script an annotated command can be used like this:
 *
 * <pre>
 * <code>
 *     CalculatorCommand method=add x=1 y=5
 * </code>
 * </pre>
 *
 * means the calculator command shall add x and y with values 1 and 5. The class should looks
 * like:
 *
 * <pre>
 * <code>
 *     public class CalculatorCommand extends AnnotatedCommand {
 *         @Param(namespace = CalculatorNamespace.class, type=ParameterType.MANDATORY, name = "METHOD")
 *         private CalculatorMethod method;
 *         @Param(namespace = CalculatorNamespace.class, name = "x")
 *         private int x;
 *         @Param(namespace = CalculatorNamespace.class, name = "y")
 *         private int y;
 *         @Param(namespace = CalculatorNamespace.class, name = "y")
 *         private int y;
 *     }
 * </code>
 * </pre>
 *
 * @author Michael Lieshoff
 */
public class AnnotatedCommand extends Command {
    private Set<String> registeredParameterKeys = new HashSet<String>();
    private Map<Param, Field> registeredParameters = new HashMap<Param, Field>();
    private Map<String, NamedParameter> namedParameters = new HashMap<String, NamedParameter>();

    public AnnotatedCommand() {
        Class<?> clazz = this.getClass();
        for (Field field : getAllFields(clazz)) {
            for(Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof Param) {
                    Param param = (Param) annotation;
                    registeredParameters.put(param, field);
                    ParameterDefinition parameterDefinition = getParameterDefinition(param);
                    for(String s : parameterDefinition.getParameterNames()) {
                        registeredParameterKeys.add(s.toLowerCase());
                    }
                }
            }
        }
    }

    private Set<Field> getAllFields(Class<?> clazz) {
        Set<Field> fields = new HashSet<Field>();
        addAllFieldsIfExists(clazz.getSuperclass(), fields);
        addAllFieldsIfExists(clazz, fields);
        return fields;
    }

    private void addAllFieldsIfExists(Class<?> clazz, Set<Field> fields) {
        for (Field field : clazz.getDeclaredFields()) {
            fields.add(field);
        }
    }

    private ParameterDefinition getParameterDefinition(Param param) {
        return (ParameterDefinition) Enum.valueOf(param.namespace(), param.name());
    }

    protected void setUp() throws IllegalAccessException {
        for(Map.Entry<Param, Field> entry : registeredParameters.entrySet()) {
            Param param = entry.getKey();
            Field field = entry.getValue();
            field.setAccessible(true);
            ParameterDefinition parameterDefinition = getParameterDefinition(param);
            checkSettingOfMandatoryParams(parameterDefinition, param);
            setField(parameterDefinition, param, field);
        }
    }

    private void checkSettingOfMandatoryParams(ParameterDefinition parameterDefinition, Param param) {
        for (String name : parameterDefinition.getParameterNames()) {
            if (getParameterByName(name) == null
                    && param.type() == ParameterType.MANDATORY) {
                throw new IllegalArgumentException(String.format(
                        "parameter '%s' is not set but was mandatory!", name));
            }
        }
    }

    private void setField(ParameterDefinition parameterDefinition, Param param, Field field)
            throws IllegalAccessException {
        Object value = parameterDefinition.resolve(param.type(), this);
        if (value != null) {
            field.set(this, value);
        }
    }

    public boolean isParameterRegistered(String parameterName) {
        return registeredParameterKeys.contains(parameterName.toLowerCase());
    }

    public void addParameter(Parameter parameter) {
        String value = parameter.stringValue();
        String[] array = computeParameterValue(value);
        if (array.length == 2) {
            value = getValue(array[1]);
            NamedParameter namedParameter = new NamedParameter(array[0], value);
            super.addParameter(namedParameter);
            namedParameters.put(namedParameter.getName(), namedParameter);
        } else {
            throw new IllegalArgumentException(String.format(
                    "parameter '%s' is not a named parameter and cannot be used in an "
                    + "annotated command!", value));
        }
    }

    private String[] computeParameterValue(String value) {
        int indexFirstEquals = value.indexOf("=");
        if (indexFirstEquals <= 0) {
            throw new IllegalArgumentException(String.format("value '%s' is not a named parameter!" , value));
        }
        return new String[]{
                value.substring(0, indexFirstEquals),
                value.substring(indexFirstEquals + 1, value.length()).replace("\\=", "="),
        };
    }

    private String getValue(String value) {
        if (ValueType.NULL.name().equals(value)) {
            return null;
        } else if (ValueType.EMPTY.name().equals(value)) {
            return "";
        }
        return value;
    }

    public Parameter getParameterByName(String parameterName) {
        return namedParameters.get(parameterName);
    }

    public boolean hasParameter(String name) {
        return namedParameters.containsKey(name);
    }

    public Collection<NamedParameter> getNamedParameters() {
        return namedParameters.values();
    }

}

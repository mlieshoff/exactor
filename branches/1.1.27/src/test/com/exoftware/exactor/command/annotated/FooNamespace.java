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

import com.exoftware.exactor.Parameter;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Michael Lieshoff
 */
public enum FooNamespace implements ParameterDefinition {
    MANDATORY_STRING(Arrays.asList("mandatoryString"), new Resolver<String, AnnotatedCommand>() {
        @Override
        public String resolve(ParameterType parameterType, AnnotatedCommand command) {
            return command.getParameterByName("mandatoryString").stringValue();
        }

        @Override
        public boolean validate(ParameterType parameterType, AnnotatedCommand command) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public List<String> getParameterNames() {
            return Arrays.asList("mandatoryString");
        }
    }),
    OPTIONAL_STRING(Arrays.asList("optionalString"), new Resolver<String, AnnotatedCommand>() {
        @Override
        public String resolve(ParameterType parameterType, AnnotatedCommand command) {
            Parameter parameter = command.getParameterByName("optionalString");
            return parameter != null ? parameter.stringValue() : null;
        }

        @Override
        public boolean validate(ParameterType parameterType, AnnotatedCommand command) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public List<String> getParameterNames() {
            return Arrays.asList("optionalString");
        }
    });

    private List<String> parameterNames;
    private Resolver resolver;

    private FooNamespace(List<String> parameterNames, Resolver resolver) {
        this.parameterNames = parameterNames;
        this.resolver = resolver;
    }

    @Override
    public List<String> getParameterNames() {
        return parameterNames;
    }

    public Object resolve(ParameterType parameterType, AnnotatedCommand command) {
        return resolver.resolve(parameterType, command);
    }

}

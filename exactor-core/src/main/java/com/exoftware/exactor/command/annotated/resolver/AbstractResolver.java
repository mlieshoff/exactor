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
package com.exoftware.exactor.command.annotated.resolver;

import com.exoftware.exactor.command.annotated.AnnotatedCommand;
import com.exoftware.exactor.command.annotated.ParameterType;
import com.exoftware.exactor.command.annotated.Resolver;
import com.exoftware.util.Require;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This abstract class defines a basic resolver.
 *
 * @author Michael Lieshoff
 */
public abstract class AbstractResolver<T, X extends AnnotatedCommand> implements Resolver<T, X> {
    private List<String> _parameterNames;

    public AbstractResolver() {
        _parameterNames = new ArrayList<String>();
    }

    public AbstractResolver(String parameterNames) {
        Require.condition(parameterNames != null, "parameter names cannot be null!");
        Require.condition(parameterNames.length() > 0, "parameter names cannot be empty!");
        _parameterNames = Arrays.asList(parameterNames.split("[,]"));
    }

    public List<String> getParameterNames() {
        return _parameterNames;
    }

    @Override
    public T resolve(ParameterType parameterType, X command) {
        List<String> problems = new ArrayList<String>();
        for (String paramName : getParameterNames()) {
            if (!command.hasParameter(paramName)) {
                problems.add(paramName + "<BR/>");
            }
        }
        if (validate(parameterType, command)) {
            return resolveIntern(command);
        } else {
            if (parameterType == ParameterType.MANDATORY) {
                throw new IllegalStateException(problems.toString());
            }
            return null;
        }
    }

    public abstract T resolveIntern(X command);
}

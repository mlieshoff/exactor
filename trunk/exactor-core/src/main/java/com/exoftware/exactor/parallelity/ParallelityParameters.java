/******************************************************************
 * Copyright (c) 2013, Exoftware
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
package com.exoftware.exactor.parallelity;

import com.exoftware.exactor.command.annotated.AnnotatedCommand;
import com.exoftware.exactor.command.annotated.ParameterDefinition;
import com.exoftware.exactor.command.annotated.ParameterType;
import com.exoftware.exactor.command.annotated.Resolver;
import com.exoftware.exactor.command.annotated.resolver.CommandResolver;
import com.exoftware.exactor.command.annotated.resolver.basic.IntegerResolver;
import com.exoftware.exactor.command.annotated.resolver.basic.LongResolver;
import com.exoftware.exactor.doc.Description;

import java.util.List;

/**
 * This enum defines parameters for parallelity variables.
 *
 * @author Michael Lieshoff
 */
public enum ParallelityParameters implements ParameterDefinition {
    @Description(text="Value for command class.")
    COMMAND(new CommandResolver("command")),
    @Description(text="Value for a pause in milli-seconds.")
    PAUSE(new LongResolver("pause")),
    @Description(text="Value for a timeout in milli-seconds.")
    TIMEOUT(new LongResolver("timeout")),
    @Description(text="Value for turns.")
    TURNS(new IntegerResolver("turns")),
    @Description(text="Value for waiting in milli-seconds.")
    WAIT(new LongResolver("wait"));

    private Resolver resolver;

    private ParallelityParameters(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public List<String> getParameterNames() {
        return resolver.getParameterNames();
    }

    @Override
    public <T> T resolve(ParameterType parameterType, AnnotatedCommand annotatedCommand) {
        return (T) resolver.resolve(parameterType, annotatedCommand);
    }

}

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
import com.exoftware.exactor.command.annotated.Param;
import com.exoftware.exactor.command.annotated.ParameterType;
import com.exoftware.util.Idioms;

/**
 * @author Michael Lieshoff
 */
public class WaitAllParallelsDone extends AnnotatedCommand {
    @Param(namespace = ParallelityParameters.class, name = "TIMEOUT", type = ParameterType.OPTIONAL)
    private long timeout = Long.getLong("com.exoftware.exactor.parallelity.WaitAllParallelsDone.timeout", 60000);

    private final Idioms.UntilTimeoutDo timeouter = new Idioms.UntilTimeoutDo() {
        @Override
        public boolean action() {
            return Gatling.finished();
        }
    };

    @Override
    public void execute() throws Exception {
        setUp();
        boolean wasTimeouted = !timeouter.run(timeout);
        if (wasTimeouted) {
            Gatling.stop();
            timeouter.run(timeout);
        }
        if (!Gatling.unregisterAll()) {
            throw new IllegalStateException("Parallels cannot be unregister!");
        }
        Gatling.stats();
    }
}

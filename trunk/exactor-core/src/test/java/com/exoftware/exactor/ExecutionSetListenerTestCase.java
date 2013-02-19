/******************************************************************
 * Copyright (c) 2005, Exoftware
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
package com.exoftware.exactor;

import junit.framework.TestCase;

/**
 * @author Brian Swan
 */
public abstract class ExecutionSetListenerTestCase extends TestCase implements ExecutionSetListener {
    protected int executionSetStartedCount;
    protected int executionSetEndedCount;
    protected int executionStartedCount;
    protected int executionEndedCount;
    protected int commandStartedCount;
    protected int commandEndedCount;
    protected Script scriptStartedParameter;
    protected Script scriptEndedParameter;
    protected Command commandedStartedParameter;
    protected Command commandedEndedParameter;
    protected Throwable commandEndedThrowable;

    public void executionSetStarted(ExecutionSet es) {
        executionSetStartedCount++;
    }

    public void executionSetEnded(ExecutionSet es) {
        executionSetEndedCount++;
    }

    public void scriptStarted(Script s) {
        scriptStartedParameter = s;
        executionStartedCount++;
    }

    public void scriptEnded(Script s) {
        scriptEndedParameter = s;
        executionEndedCount++;
    }

    public void commandStarted(Command c) {
        commandedStartedParameter = c;
        commandStartedCount++;
    }

    public void commandEnded(Command c, Throwable t) {
        commandedEndedParameter = c;
        commandEndedThrowable = t;
        commandEndedCount++;
    }
}

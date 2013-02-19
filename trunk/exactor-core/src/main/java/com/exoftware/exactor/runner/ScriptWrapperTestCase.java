/******************************************************************
 * Copyright (c) 2004, Exoftware
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
package com.exoftware.exactor.runner;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.ExecutionSet;
import com.exoftware.exactor.ExecutionSetListener;
import com.exoftware.exactor.Script;
import com.exoftware.exactor.parser.ScriptParser;
import junit.framework.TestCase;

import java.io.File;

/**
 * A TestCase that wraps and executes an acceptance test. If there is an error this is thrown
 * directly out to be caught and reported by JUnit
 *
 * @author Sean Hanly
 */

public class ScriptWrapperTestCase extends TestCase implements ExecutionSetListener {
    File file;
    Throwable throwable;
    private int lineNumber;

    public ScriptWrapperTestCase(String s) {
        super(s);
    }

    public ScriptWrapperTestCase(File file) {
        this.file = file;
        setName("testRunAcceptanceTest");
    }

    public int countTestCases() {
        return 1;
    }

    public void testRunAcceptanceTest() throws Throwable {
        if (realTest()) {
            executeTestCase();
        }
    }

    private void executeTestCase() throws Throwable {
        ExecutionSet executionSet = new ExecutionSet();
        executionSet.addListener(this);
        executionSet.addScript(new ScriptParser(executionSet).parse(file));
        executionSet.execute();
        if (throwable != null) {
            throw new Exception("Line Number: " + lineNumber, throwable);
        }
    }

    // test to see if the test is a real one as distinct from one created by
    // IDEA in its search for subclasses of TestCase
    private boolean realTest() {
        return file != null;
    }

    public String getName() {
        return (realTest()) ? file.toString() : "";
    }

    public void executionSetStarted(ExecutionSet es) {
    }

    public void executionSetEnded(ExecutionSet es) {
    }

    public void scriptStarted(Script s) {
    }

    public void scriptEnded(Script s) {
    }

    public void commandStarted(Command c) {
    }

    public void commandEnded(Command c, Throwable t) {
        if (t != null) {
            throwable = t;
            lineNumber = c.getLineNumber();
        }
    }
}

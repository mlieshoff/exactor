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
package com.exoftware.exactor.listener;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Constants;
import com.exoftware.exactor.SampleCommand;
import com.exoftware.exactor.Script;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import java.io.File;

/**
 * @author Brian Swan
 */
public class TestExecutionSummary extends TestCase {
    private ExecutionSummary summary;
    private Script scriptSingle;
    private Command command;

    protected void setUp() throws Exception {
        summary = new ExecutionSummary();
        scriptSingle = new Script(new File(Constants.pathToTestFile("single.act")));
        command = new SampleCommand();
        command.setLineNumber(1);
    }

    public void testCounts() {
        assertSummary(summary, 0, 0, 0);
        summary.scriptStarted(scriptSingle);
        assertSummary(summary, 1, 0, 0);
        summary.commandEnded(command, null);
        assertSummary(summary, 1, 0, 0);
        summary.commandEnded(command, new AssertionFailedError("A failure"));
        assertSummary(summary, 1, 1, 0);
        summary.commandEnded(command, new Exception("An error"));
        assertSummary(summary, 1, 1, 1);
    }

    public void testMultipleScripts() {
        summary.scriptStarted(scriptSingle);
        summary.scriptStarted(scriptSingle);
        assertSummary(summary, 2, 0, 0);
    }

    public void testElapsedTime() {
        summary = new ExecutionSummary() {
            int calls = 1;

            protected long getCurrentTime() {
                return 1000 * calls++;
            }
        };
        summary.executionStarted();
        assertEquals(1, summary.getElapsedTimeSeconds());
    }

    public void testDefaultConstructorSetsCalendar() {
        assertEquals(System.currentTimeMillis() / 1000, summary.getElapsedTimeSeconds());
    }

    public void testGetPackageSummaries() {
        assertEquals(0, summary.getPackageSummaries().length);
        summary.scriptStarted(scriptSingle);
        assertEquals(1, summary.getPackageSummaries().length);
    }

    public void testCommandEndedWithFailure() {
        command.setLineNumber(1);
        summary.scriptStarted(scriptSingle);
        summary.commandEnded(command, new AssertionFailedError("A failure"));
        assertFalse(summary.getPackageSummaries()[0].hasPassed());
    }

    public void testPackageSummaryNames() {
        summary = new ExecutionSummary(Constants.DATA_DIR);
        Script scriptTest1 = new Script(new File(Constants.pathToTestFile("multiplefiles" + Constants.FS
                + "test1.act")));
        summary.scriptStarted(scriptTest1);
        Script scriptTest2 = new Script(new File(Constants.pathToTestFile("multipledirs" + Constants.FS + "dir1"
                + Constants.FS + "test1.act")));
        summary.scriptStarted(scriptTest2);
        Script scriptTest3 = new Script(new File(Constants.pathToTestFile("multiplefiles" + Constants.FS + "test2.act"))
                );
        summary.scriptStarted(scriptTest3);
        assertEquals(2, summary.getPackageSummaries().length);
        assertEquals("multiplefiles", summary.getPackageSummaries()[0].getPackageName());
        assertEquals("multipledirs.dir1", summary.getPackageSummaries()[1].getPackageName());
    }

    private void assertSummary(ExecutionSummary summary, int expectedRuns, int expectedFailures, int expectedErrors) {
        assertEquals(expectedRuns, summary.getScriptsRunCount());
        assertEquals(expectedFailures, summary.getFailureCount());
        assertEquals(expectedErrors, summary.getErrorCount());
    }
}

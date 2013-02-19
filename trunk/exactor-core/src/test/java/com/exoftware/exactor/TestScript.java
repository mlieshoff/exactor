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
package com.exoftware.exactor;

import junit.framework.TestCase;

import java.io.File;

/**
 * Test class for <code>Script</code>.
 *
 * @author Brian Swan
 */
public class TestScript extends TestCase {
    private Script script;
    private MockCommand command;
    private MockExecutionSet executionSet;

    protected void setUp() throws Exception {
        script = new Script(new File(Constants.DATA_DIR + "test.act"));
        command = new MockCommand();
        executionSet = new MockExecutionSet();
    }

    public void testCreate() {
        assertEquals("test.act", script.getName());
        assertEquals(Constants.DATA_DIR + "test.act", script.getAbsolutePath());
        assertFalse(script.hasCommands());
        assertEquals(0, script.countCommands());
        assertNotNull(script.getContext());
        assertTrue(script.getContext().isEmpty());
    }

    public void testCreateWithNullName() {
        try {
            script = new Script(null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Name cannot be null", e.getMessage());
        }
    }

    public void testAddCommand() {
        script.addCommand(command);
        assertTrue(script.hasCommands());
        assertEquals(1, script.countCommands());
        assertSame(command, script.getCommand(0));
        assertSame(script, command.getScript());
    }

    public void testAddNullCommand() {
        assertFalse(script.hasCommands());
        try {
            script.addCommand(null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Command cannot be null", e.getMessage());
        }
    }

    public void testGetCommandWithNoneAdded() {
        assertFalse(script.hasCommands());
        try {
            script.getCommand(0);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 0, Size: 0", e.getMessage());
        }
    }

    public void testGetParameterWithInvalidIndex() {
        script.addCommand(command);
        try {
            script.getCommand(-1);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Size: 1", e.getMessage());
        }
        try {
            script.getCommand(2);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 2, Size: 1", e.getMessage());
        }
    }

    public void testSetExecutionSet() {
        ExecutionSet s = new ExecutionSet();
        script.setExecutionSet(s);
        assertSame(s, script.getExecutionSet());
    }

    public void testSetNullExecutionSet() {
        try {
            script.setExecutionSet(null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("ExecutionSet cannot be null", e.getMessage());
        }
    }

    public void testGetUnSetExecutionSet() {
        ExecutionSet s = script.getExecutionSet();
        assertNotNull(s);
    }

    public void testExecuteFiresExecutionSetMethods() {
        script.setExecutionSet(executionSet);
        script.addCommand(command);
        script.execute();
        assertEquals(1, executionSet.scriptStartedCount);
        assertSame(script, executionSet.scriptStartedParameter);
        assertEquals(1, executionSet.scriptEndedCount);
        assertSame(script, executionSet.scriptEndedParameter);
        assertEquals(1, executionSet.commandStartedCount);
        assertSame(command, executionSet.commandedStartedParameter);
        assertEquals(1, command.executeCalled);
        assertEquals(1, executionSet.commandEndedCount);
        assertSame(command, executionSet.commandedEndedParameter);
        assertNull(executionSet.commandEndedThrowable);
    }

    public void testExecuteWithExceptions() {
        script.setExecutionSet(executionSet);
        command.throwAssertionFailedError = true;
        script.addCommand(command);
        script.execute();
        assertNotNull(executionSet.commandEndedThrowable);
        command.throwAssertionFailedError = false;
        command.throwException = true;
        script.execute();
        assertNotNull(executionSet.commandEndedThrowable);
    }

    public void testReplaceCommand() {
        Command mockCommand = new MockCommand();
        script.addCommand(command);
        script.addCommand(mockCommand);
        Command sampleCommand = new SampleCommand();
        script.replaceCommand(mockCommand, sampleCommand);
        assertTrue(script.getCommand(1) instanceof SampleCommand);
        assertEquals(2, script.countCommands());
        assertSame(script, sampleCommand.getScript());
    }

    public void testReplaceCommandWithNulls() {
        Command c = new SampleCommand();
        try {
            script.replaceCommand(null, c);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Command cannot be null", e.getMessage());
        }
        try {
            script.addCommand(c);
            script.replaceCommand(c, null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Replacement cannot be null", e.getMessage());
        }
    }

    public void testReplaceWithNonExistantCommand() {
        try {
            script.replaceCommand(new MockCommand(), new SampleCommand());
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Command does not exist in script", e.getMessage());
        }
    }

    public void testSubstituteParameters() {
        command.addParameter(new Parameter("$0"));
        command.addParameter(new Parameter("$1"));
        Command otherCommand = new Command();
        otherCommand.addParameter(new Parameter("$0"));
        script.addCommand(command);
        script.addCommand(otherCommand);
        script.substituteParameters(new Parameter[]{new Parameter("hello"), new Parameter("world")});
        assertEquals("hello", command.getParameter(0).stringValue());
        assertEquals("world", command.getParameter(1).stringValue());
        assertEquals("hello", otherCommand.getParameter(0).stringValue());
    }

    public void testSubstituteWithNullParameters() {
        try {
            script.substituteParameters(null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Substitutions cannot be null", e.getMessage());
        }
    }
}

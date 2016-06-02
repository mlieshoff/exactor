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

import com.exoftware.exactor.command.Composite;

import java.io.File;

/**
 * Test class for <code>ExecutionSet</code>.
 *
 * @author Brian Swan
 */
public class TestExecutionSet extends ExecutionSetListenerTestCase {
    private ExecutionSet executionSet;
    private Script script;
    private Command command;

    protected void setUp() throws Exception {
        System.setProperty("exactor.blacklisted.classes", "");
        System.setProperty("exactor.class.path", "");
        executionSet = new ExecutionSet();
        script = new Script();
        command = new MockCommand();
    }

    public void testCreate() {
        assertNotNull(executionSet.getContext());
    }

    public void testAddScript() {
        executionSet.addScript(script);
        assertSame(executionSet, script.getExecutionSet());
    }

    public void testAddNullScript() {
        try {
            executionSet.addScript(null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Script cannot be null", e.getMessage());
        }
    }

    public void testAddNullListener() {
        try {
            executionSet.addListener(null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Listener cannot be null", e.getMessage());
        }
    }

    public void testFireScriptStarted() {
        executionSet.addListener(this);
        executionSet.fireScriptStarted(script);
        assertEquals(1, executionStartedCount);
        assertSame(script, scriptStartedParameter);
    }

    public void testFireScriptStartedWithNullScript() {
        try {
            executionSet.fireScriptStarted(null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Script cannot be null", e.getMessage());
        }
    }

    public void testFireScriptEnded() {
        executionSet.addListener(this);
        executionSet.fireScriptEnded(script);
        assertEquals(1, executionEndedCount);
        assertSame(script, scriptEndedParameter);
    }

    public void testFireScriptEndedWithNullScript() {
        try {
            executionSet.fireScriptEnded(null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Script cannot be null", e.getMessage());
        }
    }

    public void testFireCommandStarted() {
        executionSet.addListener(this);
        executionSet.fireCommandStarted(command);
        assertEquals(1, commandStartedCount);
        assertSame(command, commandedStartedParameter);
    }

    public void testFireCommandStartedWithNullCommand() {
        try {
            executionSet.fireCommandStarted(null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Command cannot be null", e.getMessage());
        }
    }

    public void testFireCommandEndedNoException() {
        executionSet.addListener(this);
        executionSet.fireCommandEnded(command, null);
        assertEquals(1, commandEndedCount);
        assertSame(command, commandedEndedParameter);
        assertNull(commandEndedThrowable);
    }

    public void testFireCommandEndedWithException() {
        executionSet.addListener(this);
        Exception e = new Exception();
        commandEndedCount = 0;
        executionSet.fireCommandEnded(command, e);
        assertEquals(1, commandEndedCount);
        assertSame(command, commandedEndedParameter);
        assertSame(e, commandEndedThrowable);
    }

    public void testFireCommandEndedWithNullCommand() {
        try {
            executionSet.fireCommandEnded(null, null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Command cannot be null", e.getMessage());
        }
    }

    public void testFireExecutionSetStarted() {
        executionSet.addListener(this);
        executionSet.fireExecutionSetStarted();
        assertEquals(1, executionSetStartedCount);
    }

    public void testFireExecutionSetEnded() {
        executionSet.addListener(this);
        executionSet.fireExecutionSetEnded();
        assertEquals(1, executionSetEndedCount);
    }

    public void testExecuteCallsScriptExecute() {
        executionSet.addListener(this);
        script.addCommand(command);
        executionSet.addScript(script);
        executionSet.execute();
        assertEquals(1, executionSetStartedCount);
        assertEquals(1, executionStartedCount);
        assertEquals(1, commandStartedCount);
        assertEquals(1, commandEndedCount);
        assertEquals(1, executionEndedCount);
        assertEquals(1, executionSetEndedCount);
    }

    public void testFindCommand() {
        Command c = executionSet.findCommand("MockCommand");
        assertTrue(c instanceof MockCommand);
    }

    public void testFindCommandWithActiveBlacklist() {
        System.setProperty("exactor.blacklisted.classes", "com.exoftware.exactor.MockCommand");
        executionSet = new ExecutionSet();
        Command c = executionSet.findCommand("MockCommand");
        assertNull(c);
    }

    public void testFindCommandWithActiveExactorClasspath() {
        System.setProperty("exactor.class.path", "./target/classes");
        executionSet = new ExecutionSet();
        assertNotNull(executionSet.findCommand("Print"));
    }

    public void testFindCommandWithActiveExactorClasspathButCannotFindTestClass() {
        System.setProperty("exactor.class.path", "./target/classes");
        executionSet = new ExecutionSet();
        assertNotNull(executionSet.findCommand("MockCommand"));
    }

    public void testFindNonExistantCommand() {
        assertNull(executionSet.findCommand("Junk"));
    }

    public void testFindCommandWithNullName() {
        assertNull(executionSet.findCommand(null));
    }

    public void testFindOnClasspathButNotInClassLoader() {
        Command c = executionSet.findCommand("Dummy");
        assertNotNull(c);
        assertEquals("data.Dummy", c.getClass().getName());
    }

    public void testAddCommandMapping() {
        executionSet.addCommandMapping("TestCommand", MockCommand.class);
        Command c = executionSet.findCommand("TestCommand");
        assertNotNull(c);
        assertTrue(c instanceof MockCommand);
    }

    public void testAddNullCommandMappingName() {
        try {
            executionSet.addCommandMapping(null, MockCommand.class);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Command name cannot be null", e.getMessage());
        }
    }

    public void testAddNullCommandMappingClass() {
        try {
            executionSet.addCommandMapping("TestCommand", null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Command class cannot be null", e.getMessage());
        }
    }

    public void testAddCommandMappingForNonCommandClass() {
        try {
            executionSet.addCommandMapping("Test", String.class);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Class is not a Command or Command subclass", e.getMessage());
        }
    }

    public void testFindCompositeBackwardsCompatible() {
        Command c = executionSet.findCommand("AComposite");
        assertNotNull(c);
        assertTrue(c instanceof Composite);
    }

    public void testFindCompositeOnClasspath() {
        Command c = executionSet.findCommand("comments");
        assertNotNull(c);
        assertTrue(c instanceof Composite);
    }

    public void testAddCompositeMapping() {
        executionSet.addCompositeMapping("Test", new File(Constants.DATA_DIR + "AComposite.cmp"));
        Command c = executionSet.findCommand("Test");
        assertNotNull(c);
        assertTrue(c instanceof Composite);
    }

    public void testAddNullCompositeMappingName() {
        try {
            executionSet.addCompositeMapping(null, null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Composite name cannot be null", e.getMessage());
        }
    }

    public void testAddNullCompositeMappingScript() {
        try {
            executionSet.addCompositeMapping("Test", null);
            fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Composite script cannot be null", e.getMessage());
        }
    }

    public void testContextContainsSystemProperties() {
        assertTrue(System.getProperties().containsKey("user.name"));
        assertTrue(executionSet.getContext().containsKey("user.name"));
        assertEquals(System.getProperty("user.name"), executionSet.getContext().get("user.name").toString());
    }

}

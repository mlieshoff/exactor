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

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.ExecutionSet;
import com.exoftware.exactor.MockCommand;
import com.exoftware.exactor.Script;
import com.exoftware.exactor.command.Composite;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import java.io.File;
import java.io.StringWriter;

/**
 * Test class for <code>SimpleListener</code>.
 *
 * @author Brian Swan
 */
public class TestSimpleListener extends TestCase
{
    private static final String NEW_LINE = System.getProperty( "line.separator" );
    private static final File SCRIPT_FILE = new File( Constants.DATA_DIR + "empty.act" );

    private StringWriter output;
    private SimpleListener listener;
    private MockCommand command;
    private Script script;
    private ExecutionSet executionSet;

    protected void setUp() throws Exception
    {
        output = new StringWriter();
        ExecutionSummary summary = new ExecutionSummary()
        {
            protected long getCurrentTime()
            {
                return 1000;
            }
        };
        listener = new SimpleListener( output, summary );
        script = new Script( SCRIPT_FILE );
        executionSet = new ExecutionSet();
        command = new MockCommand();
        command.setName( "TestCommand" );
        command.setLineNumber( 1 );
    }

    public void testExecutionSetStartedOutput()
    {
        listener.executionSetStarted( executionSet );
        assertEquals( "ExecutionSet started" + NEW_LINE, output.toString() );
    }

    public void testScriptStartedOutput()
    {
        listener.scriptStarted( script );
        assertEquals( "\tstarted: empty.act" + NEW_LINE, output.toString() );
    }

    public void testCommandStartedOutput()
    {
        listener.commandStarted( command );
        assertEquals( "", output.toString() );
    }

    public void testCommandEndedSuccessful()
    {
        listener.commandEnded( command, null );
        assertEquals( "\t\tOK: TestCommand" + NEW_LINE, output.toString() );
    }

    public void testCommandEndedFailure()
    {
        listener.scriptStarted( script );
        listener.commandEnded( command, new AssertionFailedError( "failure message" ) );
        assertEquals( "\tstarted: empty.act" + NEW_LINE +
                "\t\tFailed: TestCommand (" + SCRIPT_FILE.getAbsolutePath() + ":1)" + NEW_LINE +
                "\t\t\tfailure message" + NEW_LINE, output.toString() );
    }

    public void testCommandEndedError()
    {
        listener.scriptStarted( script );
        listener.commandEnded( command, new MockException( "error message" ) );
        assertEquals( "\tstarted: empty.act" + NEW_LINE +
                "\t\tError: TestCommand (" + SCRIPT_FILE.getAbsolutePath() + ":1)" + NEW_LINE +
                "\t\t\terror message" + NEW_LINE +
                "stacktrace" + NEW_LINE, output.toString() );
    }

    public void testScriptEnded()
    {
        listener.scriptEnded( script );
        assertEquals( "\tended: empty.act" + NEW_LINE, output.toString() );
    }

    public void testExecutionSetEndedNoFailuresNoErrors()
    {
        listener.executionSetEnded( executionSet );
        assertEquals( NEW_LINE + "Scripts run: 0" + NEW_LINE +
                "Failures: 0" + NEW_LINE + "" +
                "Errors: 0" + NEW_LINE + NEW_LINE +
                "Duration: 1s" + NEW_LINE, output.toString() );
    }

    public void testExecutionSetEndedOneScriptNoFailuresNoErrors()
    {
        listener.scriptStarted( script );
        listener.scriptEnded( script );
        listener.executionSetEnded( executionSet );
        assertEquals( "\tstarted: empty.act" + NEW_LINE +
                "\tended: empty.act" + NEW_LINE +
                NEW_LINE + "Scripts run: 1" + NEW_LINE +
                "Failures: 0" + NEW_LINE + "" +
                "Errors: 0" + NEW_LINE + NEW_LINE +
                "Duration: 1s" + NEW_LINE, output.toString() );
    }

    public void testExecutionSetEndedOneScriptOneFailureNoErrors()
    {
        listener.scriptStarted( script );
        listener.commandStarted( command );
        listener.commandEnded( command, new AssertionFailedError( "failure message" ) );
        listener.scriptEnded( script );
        listener.executionSetEnded( executionSet );
        assertEquals( "\tstarted: empty.act" + NEW_LINE +
                "\t\tFailed: TestCommand (" + SCRIPT_FILE.getAbsolutePath() + ":1)" + NEW_LINE +
                "\t\t\tfailure message" + NEW_LINE +
                "\tended: empty.act" + NEW_LINE +
                NEW_LINE + "Scripts run: 1" + NEW_LINE +
                "Failures: 1" + NEW_LINE + "" +
                "Errors: 0" + NEW_LINE + NEW_LINE +
                "Duration: 1s" + NEW_LINE, output.toString() );
    }

    public void testExecutionSetEndedOneScriptNoFailuresOneError()
    {
        listener.scriptStarted( script );
        listener.commandStarted( command );
        listener.commandEnded( command, new MockException( "error message" ) );
        listener.scriptEnded( script );
        listener.executionSetEnded( executionSet );
        assertEquals( "\tstarted: empty.act" + NEW_LINE +
                "\t\tError: TestCommand (" + SCRIPT_FILE.getAbsolutePath() + ":1)" + NEW_LINE +
                "\t\t\terror message" + NEW_LINE +
                "stacktrace" + NEW_LINE +
                "\tended: empty.act" + NEW_LINE +
                NEW_LINE + "Scripts run: 1" + NEW_LINE +
                "Failures: 0" + NEW_LINE + "" +
                "Errors: 1" + NEW_LINE + NEW_LINE +
                "Duration: 1s" + NEW_LINE, output.toString() );

    }

    public void testExecutionSetWithComposite()
    {
        Composite composite = new Composite( null );
        composite.setName( "Composite" );
        listener.commandStarted( composite );
        listener.commandStarted( command );
        listener.commandEnded( command, null );
        listener.commandStarted( command );
        listener.commandEnded( command, null );
        listener.commandEnded( composite, null );
        assertEquals( "\t\tOK: Composite" + NEW_LINE, output.toString() );
    }

    public void testExecutionSetWithCompositeAndCommand()
    {
        Composite composite = new Composite( null );
        composite.setName( "Composite" );
        listener.commandStarted( command );
        listener.commandEnded( command, null );
        listener.commandStarted( composite );
        listener.commandStarted( command );
        listener.commandEnded( command, null );
        listener.commandEnded( composite, null );
        assertEquals( "\t\tOK: TestCommand" + NEW_LINE +
                "\t\tOK: Composite" + NEW_LINE, output.toString() );
    }
}

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
package com.exoftware.exactor.command;

import junit.framework.AssertionFailedError;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.ExecutionSet;
import com.exoftware.exactor.ExecutionSetListenerTestCase;
import com.exoftware.exactor.MockCommand;
import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.SampleCommand;
import com.exoftware.exactor.Script;

/**
 * @author Brian Swan
 */
public class TestComposite extends ExecutionSetListenerTestCase
{
    private Script script;

    protected void setUp() throws Exception
    {
        script = new Script();
    }

    public void testSubstituteParameters() throws Exception
    {
        Command sampleCommand = new SampleCommand();
        sampleCommand.addParameter( new Parameter( "$0" ) );
        sampleCommand.addParameter( new Parameter( "$1" ) );
        script.addCommand( sampleCommand );

        Command composite = new Composite( script );
        composite.addParameter( new Parameter( "Hello" ) );
        composite.addParameter( new Parameter( "World" ) );
        composite.execute();
        assertEquals( "Hello", sampleCommand.getParameter( 0 ).stringValue() );
        assertEquals( "World", sampleCommand.getParameter( 1 ).stringValue() );
    }

    public void testExecuteScriptCommands() throws Exception
    {
        MockCommand mockCommand1 = new MockCommand();
        MockCommand mockCommand2 = new MockCommand();
        script.addCommand( mockCommand1 );
        script.addCommand( mockCommand2 );

        Composite composite = new Composite( script );
        composite.execute();
        assertEquals( 1, mockCommand1.executeCalled );
        assertEquals( 1, mockCommand2.executeCalled );
    }

    public void testExecuteWithCommandFailure() throws Exception
    {
        MockCommand command = new MockCommand();
        command.throwAssertionFailedError = true;
        script.addCommand( command );

        Composite composite = new Composite( script );
        try
        {
            composite.execute();
            fail( "AssertionFailedError not thrown" );
        }
        catch ( AssertionFailedError e )
        {
            assertEquals( "Testing assertion failure", e.getMessage() );
        }
    }


    public void testExecuteWithCommandError() throws Exception
    {
        MockCommand command = new MockCommand();
        command.throwException = true;
        script.addCommand( command );

        Composite composite = new Composite( script );
        try
        {
            composite.execute();
            fail( "Exception not thrown" );
        }
        catch ( Exception e )
        {
            assertEquals( "Testing exception", e.getMessage() );
        }
    }

    public void testScriptContext() throws Exception
    {
        Command command = new Command()
        {
            public void execute() throws Exception
            {
                assertEquals( "Value", getScript().getContext().get( "Key" ) );
            }
        };
        script.addCommand( command );

        Composite composite = new Composite( script );
        composite.getScript().getContext().put( "Key", "Value" );
        composite.execute();
    }

    public void testListnerEventsFired()
    {
        ExecutionSet set = new ExecutionSet();
        Script eventScript = new Script();
        Composite composite = new Composite( createCompositeScript() );
        eventScript.addCommand( composite );
        set.addScript( eventScript );
        set.addListener( this );
        set.execute();
        assertEquals( 1, executionSetStartedCount );
        assertEquals( 1, executionStartedCount );
        assertEquals( 3, commandStartedCount );
        assertEquals( 3, commandEndedCount );
        assertEquals( 1, executionEndedCount );
        assertEquals( 1, executionSetEndedCount );
    }

    public void testParametersAsStringWithSubstitution() throws Exception
    {
        MockCommand mockCommand = new MockCommand();
        mockCommand.addParameter( new Parameter( "$0" ) );
        mockCommand.addParameter( new Parameter( "$1" ) );

        script.addCommand( mockCommand );

        Composite composite = new Composite( script );
        composite.addParameter( new Parameter( "Hello" ) );
        composite.addParameter( new Parameter( "World" ) );
        composite.execute();

        assertEquals( "Hello World", mockCommand.parametersAsString() );
    }

    private Script createCompositeScript()
    {
        Script result = new Script();
        MockCommand command1 = new MockCommand();
        MockCommand command2 = new MockCommand();
        result.addCommand( command1 );
        result.addCommand( command2 );
        return result;
    }
}

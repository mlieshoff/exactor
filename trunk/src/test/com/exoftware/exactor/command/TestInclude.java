package com.exoftware.exactor.command;

import com.exoftware.exactor.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for <code>Include</code>.
 *
 * @author Brian Swan
 */
public class TestInclude extends ExecutionSetListenerTestCase
{
    private List executedScripts;
    private List executedCommands;
    private ExecutionSet executionSet;
    private Script script;
    private Include command;

    protected void setUp() throws Exception
    {
        executedScripts = new ArrayList();
        executedCommands = new ArrayList();
        executionSet = new ExecutionSet();
        script = new Script( new File( "test" ) );
        executionSet.addScript( script );
        executionSet.addListener( this );
        command = new Include();
        script.addCommand( command );
    }

    public void testExecute()
    {
        command.addParameter( new Parameter( Constants.DATA_DIR + "single.act" ) );
        executionSet.execute();
        assertEquals( 2, executedScripts.size() );
        assertEquals( 2, executedCommands.size() );
        assertEquals( "test", ((Script) executedScripts.get( 0 )).getName() );
        assertEquals( "single.act", ((Script) executedScripts.get( 1 )).getName() );
        assertTrue( executedCommands.get( 0 ) instanceof Include );
        assertTrue( executedCommands.get( 1 ) instanceof MockCommand );
    }

    public void scriptStarted( Script s )
    {
        executedScripts.add( s );
    }

    public void commandStarted( Command c )
    {
        executedCommands.add( c );
    }

}

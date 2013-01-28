package com.exoftware.exactor.extensions;

import junit.framework.TestCase;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.MockCommand;
import com.exoftware.exactor.Script;

public class TestPauserExtension extends TestCase
{
    private Command command;
    private Script script;
    private MyPauser pauser;

    protected void setUp() throws Exception
    {
        super.setUp();

        script  = new Script( );
        command = new MockCommand();

        script.addCommand( command );

        PauserExtension listener = new PauserExtension();
        pauser = new MyPauser();
        listener.setPauser( pauser );

        script.getExecutionSet().addListener( listener );
    }

    public void testPause()
    {
        script.execute();

        assertTrue(pauser.pauseCalled);
    }

    public void testPauseDefaultPauseValue()
    {
        script.execute();

        assertTrue(pauser.pauseCalled);
        assertEquals(1000, pauser.pauseValue);
    }

    public void testPauseTurnedOff()
    {
        script.getContext().put( PauserExtension.PAUSE_ON, "false" );
        script.execute();

        assertFalse(pauser.pauseCalled);
    }

    private class MyPauser implements Pauser
    {
        boolean pauseCalled = false;
        public long pauseValue = -1;

        public void pause( long milliseconds )
        {
            pauseCalled = true;
            pauseValue = milliseconds;
        }
    }
}

package com.exoftware.exactor.command.swing;

import java.awt.Container;

import junit.framework.TestCase;

import com.exoftware.exactor.Script;

public abstract class SwingTestCase extends TestCase
{
    protected Script script;
    protected FrameTest frame1Test;
    protected AnotherTestFrame testFrame2;

    protected void setUp() throws Exception
    {
        super.setUp();
        script = new Script( );
        frame1Test =  new FrameTest("Unit Test");
        testFrame2 =  new AnotherTestFrame("Unit Test");
        script.getContext().put( "Containers", new Container[] {frame1Test,  testFrame2} );
    }
}

package com.exoftware.exactor.command.swing;

import junit.framework.TestCase;

import javax.swing.*;

import com.exoftware.exactor.command.swing.ComponentFinder;
import com.exoftware.exactor.command.swing.AnotherTestFrame;
import com.exoftware.exactor.command.swing.FrameTest;

public class TestComponentFinder extends TestCase
{
    private FrameTest frame1;
    private AnotherTestFrame frame2;
    private JFrame[] frames;

    protected void setUp() throws Exception
    {
        super.setUp();
        frame1 = new FrameTest("Unit Test");
        frame1.setName( "Frame1" );
        frame2 = new AnotherTestFrame("Unit Test");
        frame2.setName( "Frame2" );

        frames = new JFrame[] {frame1, frame2};
    }

    public void testFindComponent_InSingleComponent()
    {
        assertSame(frame1, ComponentFinder.findComponent(frame1, "Frame1"));

        assertSame(frame1.panelTest.findControl1, ComponentFinder.findComponent(frame1, "findControl1"));
        assertSame(frame1.panelTest.findControl2, ComponentFinder.findComponent(frame1, "findControl2"));
        assertSame(frame1.panelTest.findControl3, ComponentFinder.findComponent(frame1, "findControl3"));

        assertNull( ComponentFinder.findComponent( frame1, "DoesNotExist"));
    }

    public void testFindComponent_InArray()
    {
        assertSame(frame1, ComponentFinder.findComponent(frames, "Frame1"));
        assertSame(frame2, ComponentFinder.findComponent(frames, "Frame2"));

        assertSame(frame1.panelTest.findControl1, ComponentFinder.findComponent(frames, "findControl1"));
        assertSame(frame1.panelTest.findControl2, ComponentFinder.findComponent(frames, "findControl2"));
        assertSame(frame1.panelTest.findControl3, ComponentFinder.findComponent(frames, "findControl3"));

        assertSame(frame2.jButton, ComponentFinder.findComponent(frames, "FindMe"));
    }
}

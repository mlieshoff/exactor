package com.exoftware.exactor.command.swing;

import com.exoftware.exactor.Script;
import junit.framework.TestCase;

import java.awt.*;

public abstract class SwingTestCase extends TestCase {

    protected static boolean headless = false;

    protected Script script;
    protected FrameTest frame1Test;
    protected AnotherTestFrame testFrame2;

    protected void setUp() throws Exception {
        super.setUp();
        script = new Script();
        try {
            frame1Test = new FrameTest("Unit Test");
            testFrame2 = new AnotherTestFrame("Unit Test");
        } catch (HeadlessException e) {
            headless = true;
        }
        script.getContext().put("Containers", new Container[]{frame1Test, testFrame2});
    }
}

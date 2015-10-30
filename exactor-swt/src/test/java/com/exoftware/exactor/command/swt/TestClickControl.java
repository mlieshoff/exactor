package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.command.swt.framework.TestSwt;

import java.awt.*;

public class TestClickControl extends TestSwt {

    protected void setUp() throws Exception {
        super.setUp();
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        command = new ClickControl();
        setScript();
    }

    public void test() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        addParameter("lblSwtTest");
        command.execute();
        assertTrue(testPanel.clickControlMessageSent);
    }

}

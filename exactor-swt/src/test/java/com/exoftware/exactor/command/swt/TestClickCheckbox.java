package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.command.swt.framework.TestSwt;

import java.awt.*;

public class TestClickCheckbox extends TestSwt {

    protected void setUp() throws Exception {
        super.setUp();
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        command = new ClickCheckbox();
        setScript();
    }

    public void testClickCheckbox() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        assertFalse(testPanel.check.getSelection());
        addParameter("chkTest");
        addParameter("true");
        command.execute();
        assertTrue(testPanel.check.getSelection());
    }

}

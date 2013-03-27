package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.command.swt.framework.TestSwt;

public class TestClickCheckbox extends TestSwt {
    protected void setUp() throws Exception {
        super.setUp();
        command = new ClickCheckbox();
        setScript();
    }

    public void testClickCheckbox() throws Exception {
        assertFalse(testPanel.check.getSelection());
        addParameter("chkTest");
        addParameter("true");
        command.execute();
        assertTrue(testPanel.check.getSelection());
    }

}

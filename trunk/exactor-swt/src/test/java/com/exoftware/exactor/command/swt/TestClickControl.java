package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.command.swt.framework.TestSwt;

public class TestClickControl extends TestSwt {
    protected void setUp() throws Exception {
        super.setUp();
        command = new ClickControl();
        setScript();
    }

    public void test() throws Exception {
        addParameter("lblSwtTest");
        command.execute();
        assertTrue(testPanel.clickControlMessageSent);
    }
}

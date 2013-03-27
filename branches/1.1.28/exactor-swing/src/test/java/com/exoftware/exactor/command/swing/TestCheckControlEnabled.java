package com.exoftware.exactor.command.swing;

import com.exoftware.exactor.Parameter;
import junit.framework.AssertionFailedError;

public class TestCheckControlEnabled extends SwingTestCase {
    public void testControlEnabled_Enabled() throws Exception {
        createTestCommand("enabledVisible", "true").doExecute();
        try {
            createTestCommand("enabledVisible", "false").doExecute();
            fail("exception not thrown");
        } catch (AssertionFailedError e) {
            // expect this
        }
    }

    public void testControlEnabled_Disabled() throws Exception {
        createTestCommand("notEnabled", "false").doExecute();
        try {
            createTestCommand("notEnabled", "true").doExecute();
            fail("exception not thrown");
        } catch (AssertionFailedError e) {
            // expect this
        }
    }

    private CheckControlEnabled createTestCommand(String controlName, String enabled) {
        CheckControlEnabled checkControlEnable = new CheckControlEnabled();
        checkControlEnable.addParameter(new Parameter(controlName));
        checkControlEnable.addParameter(new Parameter(enabled));
        checkControlEnable.setScript(script);
        return checkControlEnable;
    }
}

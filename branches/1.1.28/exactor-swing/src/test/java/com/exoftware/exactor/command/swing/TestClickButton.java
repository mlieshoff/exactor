package com.exoftware.exactor.command.swing;

import com.exoftware.exactor.Parameter;

public class TestClickButton extends SwingTestCase {
    public void testPustButton() throws Exception {
        ClickButton clickButton = new ClickButton();
        clickButton.addParameter(new Parameter("btnTest"));
        clickButton.setScript(script);
        clickButton.doExecute();
        assertTrue(frame1Test.panelTest.buttonClicked);
    }
}

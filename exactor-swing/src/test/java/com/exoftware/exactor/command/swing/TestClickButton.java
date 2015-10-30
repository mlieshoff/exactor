package com.exoftware.exactor.command.swing;

import com.exoftware.exactor.Parameter;

import java.awt.*;

public class TestClickButton extends SwingTestCase {

    public void testPustButton() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        ClickButton clickButton = new ClickButton();
        clickButton.addParameter(new Parameter("btnTest"));
        clickButton.setScript(script);
        clickButton.doExecute();
        assertTrue(frame1Test.panelTest.buttonClicked);
    }

}

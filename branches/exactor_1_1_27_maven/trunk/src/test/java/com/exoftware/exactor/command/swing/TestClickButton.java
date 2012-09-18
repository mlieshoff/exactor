package com.exoftware.exactor.command.swing;

import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.command.swing.ClickButton;
import com.exoftware.exactor.command.swing.SwingTestCase;

public class TestClickButton extends SwingTestCase
{
    public void testPustButton() throws Exception
    {
        ClickButton clickButton = new ClickButton();

        clickButton.addParameter( new Parameter( "btnTest" ) );
        clickButton.setScript( script );

        clickButton.doExecute();

        assertTrue(frame1Test.panelTest.buttonClicked);
    }
}

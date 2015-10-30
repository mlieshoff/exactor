package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.widgets.Control;

import java.awt.*;

public class TestControlTextSetter extends ControlTextManagerAbstractTest {

    public void testConstruction() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        ControlTextSetter.setText(button, "abc");
        assertEquals("Extracted text is wrong", "abc", button.getText());
    }

    protected void assertControlText(String expectedText, Control control) {
    }

}

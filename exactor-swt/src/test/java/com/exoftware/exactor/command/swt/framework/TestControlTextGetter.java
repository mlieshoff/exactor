package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.widgets.Control;

import java.awt.*;

public class TestControlTextGetter extends ControlTextManagerAbstractTest {

    public void testGetFromRegularControls() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        button.setText("abc");
        textBox.setText("def");
        assertControlText("abc", button);
        assertControlText("def", textBox);
        assertControlText("", classWithNoGetTextMethod);
    }

    protected void assertControlText(String expectedText, Control control) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        String text = new ControlTextGetter(control).get();
        assertEquals("Extracted text is wrong", expectedText, text);
    }

}

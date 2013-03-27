package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.widgets.Control;

public class TestControlTextGetter extends ControlTextManagerAbstractTest {
    public void testGetFromRegularControls() {
        button.setText("abc");
        textBox.setText("def");
        assertControlText("abc", button);
        assertControlText("def", textBox);
        assertControlText("", classWithNoGetTextMethod);
    }

    protected void assertControlText(String expectedText, Control control) {
        String text = new ControlTextGetter(control).get();
        assertEquals("Extracted text is wrong", expectedText, text);
    }
}

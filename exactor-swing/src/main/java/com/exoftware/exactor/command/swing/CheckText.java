package com.exoftware.exactor.command.swing;

import javax.swing.text.JTextComponent;

/**
 * Check a components text value.
 */

public class CheckText extends AbstractSwingCommand {
    public void execute() throws Exception {
        String actualText = ((JTextComponent) findComponent()).getText();
        assertEquals(getParameter(1).stringValue(), actualText);
    }
}

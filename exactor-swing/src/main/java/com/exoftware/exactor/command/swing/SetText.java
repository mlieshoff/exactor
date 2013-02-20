package com.exoftware.exactor.command.swing;

import javax.swing.text.JTextComponent;

/**
 * Set text of a component.
 */

public class SetText extends AbstractSwingAssertCommand {
    public void doExecute() throws Exception {
        ((JTextComponent) findComponent()).setText(getParameter(1).stringValue());
    }
}

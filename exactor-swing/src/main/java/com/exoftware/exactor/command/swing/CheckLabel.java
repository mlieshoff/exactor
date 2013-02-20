package com.exoftware.exactor.command.swing;

import javax.swing.*;

/**
 * Check to see if a label matches text.
 */
public class CheckLabel extends AbstractSwingCommand {
    public void doExecute() throws Exception {
        JLabel label = (JLabel) findComponent();
        assertEquals(getParameter(1).stringValue(), label.getText());
    }
}

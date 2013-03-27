package com.exoftware.exactor.command.swing;

import javax.swing.*;

/**
 * Finds a named JButton component and clicks it.
 */

public class ClickButton extends AbstractSwingAssertCommand {
    public void doExecute() throws Exception {
        ((JButton) findComponent()).doClick();
    }
}

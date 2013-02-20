package com.exoftware.exactor.command.swing;

import javax.swing.*;

/**
 * Check row count of a table.
 */
public class CheckTableRowCount extends AbstractSwingCommand {
    public void doExecute() throws Exception {
        JTable table = (JTable) findComponent();
        assertEquals(getParameter(1).intValue(), table.getRowCount());
    }
}

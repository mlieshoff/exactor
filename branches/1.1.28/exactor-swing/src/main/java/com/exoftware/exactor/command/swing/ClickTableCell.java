package com.exoftware.exactor.command.swing;

import javax.swing.*;

/**
 * Click a particular cell.
 */

public class ClickTableCell extends AbstractSwingCommand {
    protected void doExecute() throws Exception {
        JTable table = (JTable) findComponent();
        int row = getParameter(1).intValue();
        int column = getParameter(2).intValue();
        table.changeSelection(row, column, false, false);
    }
}

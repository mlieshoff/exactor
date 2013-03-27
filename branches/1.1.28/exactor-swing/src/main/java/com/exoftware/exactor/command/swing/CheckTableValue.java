package com.exoftware.exactor.command.swing;

import javax.swing.*;

/**
 * Checks a table value.
 */

public class CheckTableValue extends AbstractSwingCommand {
    public void doExecute() throws Exception {
        JTable table = (JTable) findComponent();
        int row = getParameter(1).intValue();
        int column = getParameter(2).intValue();
//          JUnit gives a incmoprehensible expected/real values
//          System.out.println("Expected ["+getParameter(3).stringValue() + "] Actual [" + actualValue + "]");
        if (table.getColumnClass(column) == Double.class) {
            Double actualValue = (Double) table.getValueAt(row, column);
            assertEquals(getParameter(3).doubleValue(), actualValue.doubleValue(), 0);
        } else {
            String actualValue = table.getValueAt(row, column).toString();
            assertEquals(getParameter(3).stringValue(), actualValue);
        }
    }
}
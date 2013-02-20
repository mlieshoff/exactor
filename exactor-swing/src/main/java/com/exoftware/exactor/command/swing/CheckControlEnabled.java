package com.exoftware.exactor.command.swing;

/**
 * Finds a named  Component and tests it's enables state
 *
 * @author Sean Hanly
 */

public class CheckControlEnabled extends AbstractSwingAssertCommand {
    public void doExecute() throws Exception {
        boolean enabled = getParameter(1).booleanValue();
        assertEquals("wrong enabled state", enabled, findComponent().isEnabled());
    }
}

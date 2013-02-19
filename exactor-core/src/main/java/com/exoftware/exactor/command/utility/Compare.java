package com.exoftware.exactor.command.utility;

import com.exoftware.exactor.Command;

/**
 * Compare two string values. For example:
 * <p/>
 * <code>
 * # passes
 * Compare abc abc
 * <p/>
 * # does not pass
 * Compare abc def
 * </code>
 */
public class Compare extends Command {
    public void execute() throws Exception {
        assertEquals(getParameter(0).stringValue(), getParameter(1).stringValue());
    }
}

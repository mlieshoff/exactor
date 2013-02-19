package com.exoftware.exactor.command.utility;

import com.exoftware.exactor.Command;

/**
 * Print to screen.
 * <p/>
 * <code>
 * # print "hello world" to screen
 * Print ""hello world"
 * </code>
 */
public class Print extends Command {
    public void execute() throws Exception {
        System.out.println(getParameter(0).stringValue());
    }
}

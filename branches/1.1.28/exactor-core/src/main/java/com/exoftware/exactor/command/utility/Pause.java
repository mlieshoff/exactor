package com.exoftware.exactor.command.utility;

import com.exoftware.exactor.Command;

/**
 * Utility command that pauses the exactor script for the number of milliseconds passed in as a parameter
 * <code>
 * # pause for a second
 * Pause 1000
 * </code>
 */
public class Pause extends Command {
    public void execute() throws Exception {
        Thread.sleep(getParameter(0).intValue());
    }
}

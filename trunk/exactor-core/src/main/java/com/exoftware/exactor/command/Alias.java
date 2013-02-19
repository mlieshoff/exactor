package com.exoftware.exactor.command;

import com.exoftware.exactor.Command;

/**
 * Command to provide an alias for an existing command. Allows the
 * same command to be referenced by multiple names.
 * <p/>
 * Usage example;
 * <pre>
 *  <code>
 *      Alias OldCommand NewCommand
 *  </code>
 * </pre>
 *
 * @author Brian Swan
 */
public class Alias extends Command {
    /**
     * Execute the command. Two parameters
     * are expected, the name of the command to alias and the new name for the command.
     * Fails if the name of the command to alias is not a currently mapped command.
     *
     * @throws Exception if an error occurs.
     */
    public void execute() throws Exception {
        String oldName = getParameter(0).stringValue();
        String newName = getParameter(1).stringValue();
        Command oldCommand = getScript().getExecutionSet().findCommand(oldName);
        if (oldCommand instanceof Composite) {
            getScript().getExecutionSet().addCompositeMapping(newName, ((Composite) oldCommand).getCompositeScriptFile()
                    );
        } else if (oldCommand != null) {
            getScript().getExecutionSet().addCommandMapping(newName, oldCommand.getClass());
        } else {
            fail("Cannot alias unknown command [" + oldName + "]");
        }
    }
}

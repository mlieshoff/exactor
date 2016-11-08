package com.exoftware.exactor.command;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Script;
import junit.framework.AssertionFailedError;

import java.io.File;

/**
 * Represents a composite command.
 */
public class Composite extends Command {
    private final Script compositeScript;

    public Composite(Script s) {
        compositeScript = s;
    }

    public void execute() throws Exception {
        try {
            compositeScript.substituteParameters(getParameters());
        } catch (Exception e) {
            //
        }
        for (int i = 0; i < compositeScript.countCommands(); i++) {
            Command c = compositeScript.getCommand(i);
            c.setScript(getScript());
            getScript().getExecutionSet().fireCommandStarted(c);
            try {
                c.execute();
            } catch (AssertionFailedError e) {
                getScript().getExecutionSet().fireCommandEnded(c, e);
                throw e;
            } catch (Exception e) {
                getScript().getExecutionSet().fireCommandEnded(c, e);
                throw e;
            }
            getScript().getExecutionSet().fireCommandEnded(c, null);
        }
    }

    public Script getCompositeScript() {
        return compositeScript;
    }

    public File getCompositeScriptFile() {
        return new File(compositeScript.getAbsolutePath());
    }
}

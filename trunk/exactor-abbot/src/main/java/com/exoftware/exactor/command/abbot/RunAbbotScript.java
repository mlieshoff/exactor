package com.exoftware.exactor.command.abbot;

import abbot.Log;
import abbot.finder.AWTHierarchy;
import abbot.finder.Hierarchy;
import abbot.finder.TestHierarchy;
import abbot.script.Script;
import abbot.script.StepRunner;
import com.exoftware.exactor.Command;
import junit.extensions.abbot.AWTFixtureHelper;

/**
 * Exactor command for launching and executing an Abbot XML script.
 */

public class RunAbbotScript extends Command {
    private AWTFixtureHelper helper;
    private Hierarchy hierarchy;

    public void execute() throws Exception {
        String filename = getParameter(0).stringValue();
        setUp(filename);
        try {
            runScript(filename);
        } catch (Throwable throwable) {
            throw new RuntimeException("Error running: " + filename, throwable);
        }
        tearDown();
    }


    protected void setUp(String filename) {
        helper = new AWTFixtureHelper();
        boolean hasLaunch = new Script(filename, new AWTHierarchy()).hasLaunch();
        hierarchy = new TestHierarchy(hasLaunch);
        // Support for deprecated ComponentTester.assertFrameShowing usage
        // only.  Eventually this will go away.
        AWTHierarchy.setDefault(hierarchy);
    }

    protected void tearDown() {
        hierarchy = null;
        AWTHierarchy.setDefault(null);
        helper.restore();
        helper = null;
    }

    protected void runScript(String filename) throws Throwable {
        Script script = new Script(filename, hierarchy);
        Log.log("Running " + script + " with " + getClass());
        StepRunner runner = new StepRunner(hierarchy);
        try {
            runner.run(script);
        } finally {
            Log.log(script.toString() + " finished");
        }
    }
}

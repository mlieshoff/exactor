package com.exoftware.exactor.command.web;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Script;
import junit.framework.TestCase;

/**
 * Base class for all tests of <code>WebCommand</code> subclasses.
 *
 * @author Brian Swan
 */
public abstract class WebCommandTestCase extends TestCase {
    protected Script script;
    protected MockWebTester webTester;
    protected Command command;

    protected void setUp() throws Exception {
        script = new Script();
        webTester = new MockWebTester();
        script.getContext().put("webtester", webTester);
        command = getCommand();
        script.addCommand(command);
    }

    protected abstract Command getCommand();
}

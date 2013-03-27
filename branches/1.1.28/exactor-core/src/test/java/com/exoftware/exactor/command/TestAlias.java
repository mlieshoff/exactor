package com.exoftware.exactor.command;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Constants;
import com.exoftware.exactor.MockCommand;
import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.Script;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import java.io.File;

/**
 * @author Brian Swan
 */
public class TestAlias extends TestCase {
    private Alias alias;
    private Script script;

    protected void setUp() throws Exception {
        alias = new Alias();
        alias.getScript().getExecutionSet().addCommandMapping("MockCommand", MockCommand.class);
        File scriptFile = new File(Constants.DATA_DIR + "single.act");
        script = new Script(scriptFile);
        alias.getScript().getExecutionSet().addCompositeMapping("CompositeCommand", scriptFile);
    }

    public void testExecute() throws Exception {
        alias.addParameter(new Parameter("MockCommand"));
        alias.addParameter(new Parameter("NewCommand"));
        assertNotNull(alias.getScript().getExecutionSet().findCommand("MockCommand"));
        assertNull(alias.getScript().getExecutionSet().findCommand("NewCommand"));
        alias.execute();
        Command aliasedCommand = alias.getScript().getExecutionSet().findCommand("NewCommand");
        assertNotNull(aliasedCommand);
        assertTrue(aliasedCommand instanceof MockCommand);
        assertNotNull(alias.getScript().getExecutionSet().findCommand("MockCommand"));
    }

    public void testCommandToAliasDoesNotExist() throws Exception {
        alias.addParameter(new Parameter("NewCommand"));
        alias.addParameter(new Parameter("NewNewCommand"));
        assertNull(alias.getScript().getExecutionSet().findCommand("NewCommand"));
        try {
            alias.execute();
            fail("AssertionFailedError not thrown");
        } catch (AssertionFailedError e) {
            assertEquals("Cannot alias unknown command [NewCommand]", e.getMessage());
            assertNull(alias.getScript().getExecutionSet().findCommand("NewCommand"));
            assertNull(alias.getScript().getExecutionSet().findCommand("NewNewCommand"));
        }
    }

    public void testCommandToAliasIsAComposite() throws Exception {
        alias.addParameter(new Parameter("CompositeCommand"));
        alias.addParameter(new Parameter("NewCommand"));
        assertNull(alias.getScript().getExecutionSet().findCommand("NewCommand"));
        alias.execute();
        Composite aliasedCommand = (Composite) alias.getScript().getExecutionSet().findCommand("NewCommand");
        assertNotNull(aliasedCommand);
        assertEquals(script.getName(), aliasedCommand.getCompositeScript().getName());
    }

}

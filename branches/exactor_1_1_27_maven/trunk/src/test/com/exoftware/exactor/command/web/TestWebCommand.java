package com.exoftware.exactor.command.web;

import com.exoftware.exactor.Script;
import junit.framework.TestCase;

/**
 * Test class for <code>WebCommand</code>.
 *
 * @author Brian Swan
 */
public class TestWebCommand extends TestCase
{
    private Script script;
    private WebCommand command;

    protected void setUp() throws Exception
    {
        script = new Script();
        command = new WebCommand();
        script.addCommand( command );
    }

    public void testGetWebTesterAddsToContext()
    {
        assertFalse( script.getContext().containsKey( "webtester" ) );
        assertNotNull( command.getWebTester() );
        assertTrue( script.getContext().containsKey( "webtester" ) );
    }

    public void testGetWebTesterRetrievesFromContext()
    {
        MockWebTester mockWebTester = new MockWebTester();
        script.getContext().put( "webtester", mockWebTester );
        assertSame( mockWebTester, command.getWebTester() );
    }
}

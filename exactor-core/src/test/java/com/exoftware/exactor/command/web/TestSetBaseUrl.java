package com.exoftware.exactor.command.web;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Parameter;

/**
 * Test class for <code>SetBaseUrl</code>.
 *
 * @author Brian Swan
 */
public class TestSetBaseUrl extends WebCommandTestCase
{
    protected Command getCommand()
    {
        return new SetBaseUrl();
    }

    public void testDelagates() throws Exception
    {
        command.addParameter( new Parameter( "http://www.google.com" ) );
        command.execute();
        MockTestContext testContext = (MockTestContext) webTester.getTestContext();
        assertEquals( "http://www.google.com", testContext.getBaseUrl() );
    }
}

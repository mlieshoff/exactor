package com.exoftware.exactor.command;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * Test class for <code>Unknown</code>.
 *
 * @author Brian Swan
 */
public class TestUnknown extends TestCase
{
    public void testExecute() throws Exception
    {
        try
        {
            new Unknown( "SomeCommand" ).execute();
            fail( "AssertionFailedError not thrown" );
        }
        catch( AssertionFailedError e )
        {
            assertEquals( "No command found for: SomeCommand", e.getMessage() );
        }

    }
}

package com.exoftware.exactor.command.swt;

import junit.framework.ComparisonFailure;

import com.exoftware.exactor.command.swt.framework.TestSwt;

public class TestCheckSelection extends TestSwt
{
    protected void setUp() throws Exception
    {
        super.setUp();
        command = new CheckSelection();
        setScript();
    }

    public void testSelection() throws Exception
    {
        addParameter( "chkTest" );
        addParameter( "true" );
        try
        {
            command.execute();
            fail( "should have thrown comparison exception" );
        }
        catch( ComparisonFailure e )
        {
            assertEquals( "wrong value expected:<tru...> but was:<fals...>", e.getMessage() );
        }
    }
}

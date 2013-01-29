package com.exoftware.exactor.command.swt;

import junit.framework.ComparisonFailure;

import com.exoftware.exactor.command.swt.framework.TestSwt;

public class TestCheckControlVisible extends TestSwt
{
    protected void setUp() throws Exception
    {
        super.setUp();
        command = new CheckControlVisible();
        setScript();
    }

    public void testControlIsVisible() throws Exception
    {
        addParameter( "txtTest" );
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

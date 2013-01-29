package com.exoftware.exactor.command.swt;

import junit.framework.ComparisonFailure;

import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.command.swt.framework.TestSwt;

public class TestCheckControlEnabled extends TestSwt
{
    protected void setUp() throws Exception
    {
        super.setUp();
        command = new CheckControlEnabled();
        setScript();
    }

    public void testControlEnabled() throws Exception
    {
        command.addParameter( new Parameter( "txtDisabled" ) );
        command.addParameter( new Parameter( "true" ) );

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

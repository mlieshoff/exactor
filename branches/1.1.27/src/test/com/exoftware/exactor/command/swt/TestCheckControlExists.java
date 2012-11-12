package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.command.swt.framework.TestSwt;

public class TestCheckControlExists extends TestSwt
{
    protected void setUp() throws Exception
    {
        super.setUp();
        command = new CheckControlExists();
    }


    public void testControlExists() throws Exception
    {
        command.addParameter( new Parameter( "txtNotExists" ) );
        command.addParameter( new Parameter( "true" ) );
        try
        {
            command.execute();
            fail( "should have thrown comparison exception" );
        }
        catch( Throwable e )
        {
            assertEquals( "wrong value expected:<true> but was:<false>", e.getMessage() );
        }

    }
}

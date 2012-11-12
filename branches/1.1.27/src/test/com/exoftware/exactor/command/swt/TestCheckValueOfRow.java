package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.command.swt.framework.TestSwt;

public class TestCheckValueOfRow extends TestSwt
{
    protected void setUp() throws Exception
    {
        super.setUp();
        command = new CheckValueOfRow();
        setScript();
    }

    public void testValueOfRow() throws Exception
    {
        addParameter( "tblTable(0)" );
        addParameter( "xx,xx" );
    }
}

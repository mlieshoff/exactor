package com.exoftware.exactor.command.utility;

import junit.framework.TestCase;

import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.Script;

public class TestSetProperty extends TestCase
{
    public void testCommand() throws Exception
    {
        Script script = new Script();
        SetProperty setProperty = new SetProperty();

        setProperty.addParameter( new Parameter( "key" ) );
        setProperty.addParameter( new Parameter( "value" ) );

        setProperty.setScript( script );

        setProperty.execute();

        assertEquals( "value", script.getContext().get( "key" ) );
    }
}

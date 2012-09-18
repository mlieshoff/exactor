package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.widgets.Control;

public class TestControlTextSetter extends ControlTextManagerAbstractTest
{
    public void testConstruction()
    {
        ControlTextSetter.setText( button, "abc" );
        assertEquals( "Extracted text is wrong", "abc", button.getText() );
    }

    protected void assertControlText( String expectedText, Control control )
    {

    }
}

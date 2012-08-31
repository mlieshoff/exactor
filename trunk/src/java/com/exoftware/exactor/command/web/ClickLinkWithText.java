package com.exoftware.exactor.command.web;

/**
 * Command wrapping jwebunit <code>WebTester.clickLinkWithText( String, int }</code>
 * and <code>WebTester.clickLinkWithText( String }</code>.
 * <p/>
 * Usage example;
 * <pre>
 *  <code>
 *      ClickLinkWithText linkText [index]
 *  </code>
 * </pre>
 *
 * @author Brian Swan
 */
public class ClickLinkWithText extends WebCommand
{
    /**
     * Execute the command. Navigate by selection of a link containing given text.
     * One parameter is expected, linkText. If more than one link with the same text is expected
     * an optional 0 based parameter can be supplied.
     *
     * @throws Exception is an error occurs.
     */
    public void execute() throws Exception
    {
        if( countParameters() == 2 )
            getWebTester().clickLinkWithText( getParameter( 0 ).stringValue(), getParameter( 1 ).intValue() );
        else
            getWebTester().clickLinkWithText( getParameter( 0 ).stringValue() );
    }
}
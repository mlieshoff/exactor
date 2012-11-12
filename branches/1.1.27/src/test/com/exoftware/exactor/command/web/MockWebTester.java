package com.exoftware.exactor.command.web;

import net.sourceforge.jwebunit.ExpectedTable;
import net.sourceforge.jwebunit.HttpUnitDialog;
import net.sourceforge.jwebunit.TestContext;
import net.sourceforge.jwebunit.WebTester;

import java.io.PrintStream;

/**
 * @author Brian Swan
 */
public class MockWebTester extends WebTester
{
    private MockTestContext testContext = new MockTestContext();

    String beginAt;

    public HttpUnitDialog getDialog()
    {
        return null;
    }

    public TestContext getTestContext()
    {
        return testContext;
    }

    public void beginAt( String s )
    {
        beginAt = s;
    }

    public String getMessage( String s )
    {
        return null;
    }

    public void assertTitleEquals( String s )
    {
    }

    public void assertTitleEqualsKey( String s )
    {
    }

    public void assertKeyPresent( String s )
    {
    }

    public void assertTextPresent( String s )
    {
    }

    public void assertKeyNotPresent( String s )
    {
    }

    public void assertTextNotPresent( String s )
    {
    }

    public void assertTablePresent( String s )
    {
    }

    public void assertTableNotPresent( String s )
    {
    }

    public void assertKeyInTable( String s, String s1 )
    {
    }

    public void assertTextInTable( String s, String s1 )
    {
    }

    public void assertKeysInTable( String s, String[] strings )
    {
    }

    public void assertTextInTable( String s, String[] strings )
    {
    }

    public void assertKeyNotInTable( String s, String s1 )
    {
    }

    public void assertTextNotInTable( String s, String s1 )
    {
    }

    public void assertTextNotInTable( String s, String[] strings )
    {
    }

    public void assertTableEquals( String s, ExpectedTable expectedTable )
    {
    }

    public void assertTableEquals( String s, String[][] strings )
    {
    }

    public void assertTableRowsEqual( String s, int i, ExpectedTable expectedTable )
    {
    }

    public void assertTableRowsEqual( String s, int i, String[][] strings )
    {
    }

    public void assertFormElementPresent( String s )
    {
    }

    public void assertFormElementNotPresent( String s )
    {
    }

    public void assertFormElementPresentWithLabel( String s )
    {
    }

    public void assertFormElementNotPresentWithLabel( String s )
    {
    }

    public void assertFormPresent()
    {
    }

    public void assertFormPresent( String s )
    {
    }

    public void assertFormNotPresent()
    {
    }

    public void assertFormNotPresent( String s )
    {
    }

    public void assertFormElementEquals( String s, String s1 )
    {
    }

    public void assertFormElementEmpty( String s )
    {
    }

    public void assertCheckboxSelected( String s )
    {
    }

    public void assertCheckboxNotSelected( String s )
    {
    }

    public void assertRadioOptionPresent( String s, String s1 )
    {
    }

    public void assertRadioOptionNotPresent( String s, String s1 )
    {
    }

    public void assertRadioOptionSelected( String s, String s1 )
    {
    }

    public void assertRadioOptionNotSelected( String s, String s1 )
    {
    }

    public void assertOptionsEqual( String s, String[] strings )
    {
    }

    public void assertOptionsNotEqual( String s, String[] strings )
    {
    }

    public void assertOptionValuesEqual( String s, String[] strings )
    {
    }

    public void assertOptionValuesNotEqual( String s, String[] strings )
    {
    }

    public void assertOptionEquals( String s, String s1 )
    {
    }

    public void assertSubmitButtonPresent( String s )
    {
    }

    public void assertSubmitButtonNotPresent( String s )
    {
    }

    public void assertSubmitButtonValue( String s, String s1 )
    {
    }

    public void assertButtonPresent( String s )
    {
    }

    public void assertButtonNotPresent( String s )
    {
    }

    public void assertLinkPresent( String s )
    {
    }

    public void assertLinkNotPresent( String s )
    {
    }

    public void assertLinkPresentWithText( String s )
    {
    }

    public void assertLinkNotPresentWithText( String s )
    {
    }

    public void assertLinkPresentWithText( String s, int i )
    {
    }

    public void assertLinkNotPresentWithText( String s, int i )
    {
    }

    public void assertLinkPresentWithImage( String s )
    {
    }

    public void assertLinkNotPresentWithImage( String s )
    {
    }

    public void assertElementPresent( String s )
    {
    }

    public void assertElementNotPresent( String s )
    {
    }

    public void assertTextInElement( String s, String s1 )
    {
    }

    public void assertTextNotInElement( String s, String s1 )
    {
    }

    public void assertWindowPresent( String s )
    {
    }

    public void assertFramePresent( String s )
    {
    }

    public void assertCookiePresent( String s )
    {
    }

    public void assertCookieValueEquals( String s, String s1 )
    {
    }

    public void dumpCookies()
    {
    }

    public void dumpCookies( PrintStream printStream )
    {
    }

    public void setWorkingForm( String s )
    {
    }

    public void setFormElement( String s, String s1 )
    {
    }

    protected void setFormElementWithLabel( String s, String s1 )
    {
    }

    public void checkCheckbox( String s )
    {
    }

    public void checkCheckbox( String s, String s1 )
    {
    }

    public void uncheckCheckbox( String s )
    {
    }

    public void uncheckCheckbox( String s, String s1 )
    {
    }

    public void selectOption( String s, String s1 )
    {
    }

    public void submit()
    {
    }

    public void submit( String s )
    {
    }

    public void reset()
    {
    }

    public void clickLinkWithText( String s )
    {
    }

    public void clickLinkWithText( String s, int i )
    {
    }

    public void clickLinkWithTextAfterText( String s, String s1 )
    {
    }

    public void clickButton( String s )
    {
    }

    public void clickLinkWithImage( String s )
    {
    }

    public void clickLink( String s )
    {
    }

    public void gotoWindow( String s )
    {
    }

    public void gotoRootWindow()
    {
    }

    public void gotoFrame( String s )
    {
    }

    public void gotoPage( String s )
    {
    }

    public void dumpResponse()
    {
    }

    public void dumpResponse( PrintStream printStream )
    {
    }

    public void dumpTable( String s, PrintStream printStream )
    {
    }

    public void dumpTable( String s, String[][] strings )
    {
    }

    public void dumpTable( String s, String[][] strings, PrintStream printStream )
    {
    }
}

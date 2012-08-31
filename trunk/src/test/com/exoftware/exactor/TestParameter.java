package com.exoftware.exactor;

import junit.framework.TestCase;

/**
 * Test class for <code>Parameter</code>.
 *
 * @author Brian Swan
 */
public class TestParameter extends TestCase
{
    private Parameter parameter;
    private SampleCommand command;

    protected void setUp() throws Exception
    {
        parameter = new Parameter( "hello" );
        command = new SampleCommand();
        command.getScript().getContext().put( "key1", "hello" );
        command.getScript().getContext().put( "key2", "world" );
    }

    public void testCreateStringParameter()
    {
        assertEquals( "hello", parameter.stringValue() );
        assertEquals( "hello", parameter.toString() );
        assertFalse( parameter.isNumeric() );
    }

    public void testCreateIntParameter()
    {
        parameter = new Parameter( "10" );
        assertEquals( "10", parameter.stringValue() );
        assertTrue( parameter.isNumeric() );
        assertEquals( 10, parameter.intValue() );
    }

    public void testCreateNegativeIntParameter()
    {
        parameter = new Parameter( "-10" );
        assertEquals( "-10", parameter.stringValue() );
        assertTrue( parameter.isNumeric() );
        assertEquals( -10, parameter.intValue() );
    }

    public void testCreateDoubleParameter()
    {
        parameter = new Parameter( "10.52" );
        assertEquals( "10.52", parameter.stringValue() );
        assertTrue( parameter.isNumeric() );
        assertEquals( 10.52, parameter.doubleValue(), 0 );
    }

    public void testCreateBooleanParameter()
    {
        parameter = new Parameter( "true" );
        assertEquals( "true", parameter.stringValue() );
        assertFalse( parameter.isNumeric() );
        assertTrue( parameter.booleanValue() );
    }

    public void testIntValueOfNonIntParameter()
    {
        assertFalse( parameter.isNumeric() );
        try
        {
            parameter.intValue();
            fail( "NumberFormatException not thrown" );
        }
        catch( NumberFormatException e )
        {
            assertEquals( "For input string: \"hello\"", e.getMessage() );
        }
    }

    public void testDoubleValueOfNonDoubleParameter()
    {
        assertFalse( parameter.isNumeric() );
        try
        {
            parameter.doubleValue();
            fail( "NumberFormatException not thrown" );
        }
        catch( NumberFormatException e )
        {
            assertEquals( "For input string: \"hello\"", e.getMessage() );
        }
    }

    public void testBooleanValueOfNonBooleanParameter()
    {
        assertFalse( parameter.booleanValue() );
    }

    public void testSetNullCommand()
    {
        try
        {
            parameter.setCommand( null );
            fail( "RuntimeExcaption not thrown" );
        }
        catch( RuntimeException e )
        {
            assertEquals( "Command cannot be null", e.getMessage() );
        }
    }

    public void testSetCommand()
    {
        SampleCommand command = new SampleCommand();
        parameter.setCommand( command );
        assertSame( command, parameter.getCommand() );
    }

    public void testSubstitutionFromScriptContext()
    {
        command.getScript().getContext().put( "test", "testvalue" );
        parameter = new Parameter( "[test]" );
        parameter.setCommand( command );
        assertEquals( "testvalue", parameter.stringValue() );
    }

    public void testSubstitutionWithNoMatchingKey()
    {
        parameter = new Parameter( "[test]" );
        parameter.setCommand( command );
        assertEquals( "[test]", parameter.stringValue() );
    }

    public void testSubstitutionInString()
    {
        command.getScript().getContext().put( "test", "testvalue" );
        parameter = new Parameter( "value:[test]" );
        parameter.setCommand( command );
        assertEquals( "value:testvalue", parameter.stringValue() );
    }

    public void testNoOpeningBracket()
    {
        command.getScript().getContext().put( "test", "testvalue" );
        parameter = new Parameter( "test]" );
        parameter.setCommand( command );
        assertEquals( "test]", parameter.stringValue() );
    }

    public void testNoClosingBracket()
    {
        command.getScript().getContext().put( "test", "testvalue" );
        parameter = new Parameter( "[test" );
        parameter.setCommand( command );
        assertEquals( "[test", parameter.stringValue() );
    }

    public void testClosingBracketBeforeOpeningBracket()
    {
        command.getScript().getContext().put( "test", "testvalue" );
        parameter = new Parameter( "]test[" );
        parameter.setCommand( command );
        assertEquals( "]test[", parameter.stringValue() );
    }

    public void testEmptyKey()
    {
        command.getScript().getContext().put( "test", "testvalue" );
        parameter = new Parameter( "[]" );
        parameter.setCommand( command );
        assertEquals( "[]", parameter.stringValue() );
    }

    public void testSubstitutionFromExecutionContext()
    {
        command.getScript().getExecutionSet().getContext().put( "test", "testvalue" );
        parameter = new Parameter( "[test]" );
        parameter.setCommand( command );
        assertEquals( "testvalue", parameter.stringValue() );
    }

    public void testSubstitutionFromScriptContextOverridesExecutionContext()
    {
        command.getScript().getExecutionSet().getContext().put( "test", "XXX" );
        command.getScript().getContext().put( "test", "testvalue" );
        parameter = new Parameter( "[test]" );
        parameter.setCommand( command );
        assertEquals( "testvalue", parameter.stringValue() );
    }

    public void testNumericSubstitution()
    {
        command.getScript().getContext().put( "testint", "123" );
        command.getScript().getContext().put( "testdouble", "1.275" );
        parameter = new Parameter( "[testint]" );
        parameter.setCommand( command );
        assertEquals( 123, parameter.intValue() );

        parameter = new Parameter( "[testdouble]" );
        parameter.setCommand( command );
        assertEquals( 1.275, parameter.doubleValue(), 0 );
    }

    public void testBooleanSubstitution()
    {
        command.getScript().getContext().put( "testtrue", "true" );
        command.getScript().getContext().put( "testfalse", "anything" );
        parameter = new Parameter( "[testtrue]" );
        parameter.setCommand( command );
        assertTrue( parameter.booleanValue() );
        parameter = new Parameter( "[testfalse]" );
        parameter.setCommand( command );
        assertFalse( parameter.booleanValue() );
    }

    public void testMultipleSubstitutions()
    {
        command.getScript().getContext().put( "def", "12" );
        command.getScript().getContext().put( "ij", "3456" );
        parameter = new Parameter( "abc[def]gh[ij]kl[m][]" );
        parameter.setCommand( command );

        assertEquals( "abc12gh3456kl[m][]", parameter.stringValue() );
    }

}

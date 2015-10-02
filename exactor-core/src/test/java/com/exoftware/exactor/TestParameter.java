package com.exoftware.exactor;

import com.exoftware.exactor.command.annotated.NamedParameter;
import junit.framework.TestCase;

/**
 * Test class for <code>Parameter</code>.
 *
 * @author Brian Swan
 */
public class TestParameter extends TestCase {
    private Parameter parameter;
    private SampleCommand command;

    protected void setUp() throws Exception {
        parameter = new Parameter("hello");
        command = new SampleCommand();
        command.getScript().getContext().put("key1", "hello");
        command.getScript().getContext().put("key2", "world");
    }

    public void testCreateStringParameter() {
        assertEquals("hello", parameter.stringValue());
        assertEquals("hello", parameter.toString());
        assertFalse(parameter.isNumeric());
    }

    public void testCreateIntParameter() {
        parameter = new Parameter("10");
        assertEquals("10", parameter.stringValue());
        assertTrue(parameter.isNumeric());
        assertEquals(10, parameter.intValue());
    }

    public void testCreateNegativeIntParameter() {
        parameter = new Parameter("-10");
        assertEquals("-10", parameter.stringValue());
        assertTrue(parameter.isNumeric());
        assertEquals(-10, parameter.intValue());
    }

    public void testCreateDoubleParameter() {
        parameter = new Parameter("10.52");
        assertEquals("10.52", parameter.stringValue());
        assertTrue(parameter.isNumeric());
        assertEquals(10.52, parameter.doubleValue(), 0);
    }

    public void testCreateFloatParameter() {
        parameter = new Parameter("10.52");
        assertEquals("10.52", parameter.stringValue());
        assertTrue(parameter.isNumeric());
        assertEquals(Float.valueOf("10.52"), parameter.floatValue(), 0);
    }

    public void testCreateBooleanParameter() {
        parameter = new Parameter("true");
        assertEquals("true", parameter.stringValue());
        assertFalse(parameter.isNumeric());
        assertTrue(parameter.booleanValue());
    }

    public void testIntValueOfNonIntParameter() {
        assertFalse(parameter.isNumeric());
        try {
            parameter.intValue();
            fail("NumberFormatException not thrown");
        } catch (NumberFormatException e) {
            assertEquals("For input string: \"hello\"", e.getMessage());
        }
    }

    public void testDoubleValueOfNonDoubleParameter() {
        assertFalse(parameter.isNumeric());
        try {
            parameter.doubleValue();
            fail("NumberFormatException not thrown");
        } catch (NumberFormatException e) {
            assertEquals("For input string: \"hello\"", e.getMessage());
        }
    }

    public void testFloatValueOfNonFloatParameter() {
        assertFalse(parameter.isNumeric());
        try {
            parameter.floatValue();
            fail("NumberFormatException not thrown");
        } catch (NumberFormatException e) {
            assertEquals("For input string: \"hello\"", e.getMessage());
        }
    }

    public void testBooleanValueOfNonBooleanParameter() {
        assertFalse(parameter.booleanValue());
    }

    public void testLongValue() {
        command.addParameter(new Parameter("4711"));
        assertEquals(4711L, command.getParameter(0).longValue());
    }

    public void testLongValueOfNonLongParameter() {
        assertFalse(parameter.isNumeric());
        try {
            parameter.longValue();
            fail("NumberFormatException not thrown");
        } catch (NumberFormatException e) {
            assertEquals("For input string: \"hello\"", e.getMessage());
        }
    }

    public void testSetNullCommand() {
        try {
            parameter.setCommand(null);
            fail("RuntimeExcaption not thrown");
        } catch (RuntimeException e) {
            assertEquals("Command cannot be null", e.getMessage());
        }
    }

    public void testSetCommand() {
        SampleCommand c = new SampleCommand();
        parameter.setCommand(c);
        assertSame(c, parameter.getCommand());
    }

    public void testSubstitutionFromScriptContext() {
        command.getScript().getContext().put("test", "testvalue");
        parameter = new Parameter("[test]");
        parameter.setCommand(command);
        assertEquals("testvalue", parameter.stringValue());
    }

    public void testSubstitutionWithNoMatchingKey() {
        parameter = new Parameter("[test]");
        parameter.setCommand(command);
        assertEquals("[test]", parameter.stringValue());
    }

    public void testSubstitutionInString() {
        command.getScript().getContext().put("test", "testvalue");
        parameter = new Parameter("value:[test]");
        parameter.setCommand(command);
        assertEquals("value:testvalue", parameter.stringValue());
    }

    public void testNoOpeningBracket() {
        command.getScript().getContext().put("test", "testvalue");
        parameter = new Parameter("test]");
        parameter.setCommand(command);
        assertEquals("test]", parameter.stringValue());
    }

    public void testNoClosingBracket() {
        command.getScript().getContext().put("test", "testvalue");
        parameter = new Parameter("[test");
        parameter.setCommand(command);
        assertEquals("[test", parameter.stringValue());
    }

    public void testClosingBracketBeforeOpeningBracket() {
        command.getScript().getContext().put("test", "testvalue");
        parameter = new Parameter("]test[");
        parameter.setCommand(command);
        assertEquals("]test[", parameter.stringValue());
    }

    public void testEmptyKey() {
        command.getScript().getContext().put("test", "testvalue");
        parameter = new Parameter("[]");
        parameter.setCommand(command);
        assertEquals("[]", parameter.stringValue());
    }

    public void testSubstitutionFromExecutionContext() {
        command.getScript().getExecutionSet().getContext()
                .put("test", "testvalue");
        parameter = new Parameter("[test]");
        parameter.setCommand(command);
        assertEquals("testvalue", parameter.stringValue());
    }

    public void testSubstitutionFromScriptContextOverridesExecutionContext() {
        command.getScript().getExecutionSet().getContext().put("test", "XXX");
        command.getScript().getContext().put("test", "testvalue");
        parameter = new Parameter("[test]");
        parameter.setCommand(command);
        assertEquals("testvalue", parameter.stringValue());
    }

    public void testNumericSubstitution() {
        command.getScript().getContext().put("testint", "123");
        command.getScript().getContext().put("testdouble", "1.275");
        parameter = new Parameter("[testint]");
        parameter.setCommand(command);
        assertEquals(123, parameter.intValue());

        parameter = new Parameter("[testdouble]");
        parameter.setCommand(command);
        assertEquals(1.275, parameter.doubleValue(), 0);
    }

    public void testBooleanSubstitution() {
        command.getScript().getContext().put("testtrue", "true");
        command.getScript().getContext().put("testfalse", "anything");
        parameter = new Parameter("[testtrue]");
        parameter.setCommand(command);
        assertTrue(parameter.booleanValue());
        parameter = new Parameter("[testfalse]");
        parameter.setCommand(command);
        assertFalse(parameter.booleanValue());
    }

    public void testMultipleSubstitutions() {
        command.getScript().getContext().put("def", "12");
        command.getScript().getContext().put("ij", "3456");
        parameter = new Parameter("abc[def]gh[ij]kl[m][]");
        parameter.setCommand(command);
        assertEquals("abc12gh3456kl[m][]", parameter.stringValue());
    }

    public void testSplittedValue() {
        parameter = new Parameter("a,b,c");
        String[] array = parameter.splittedString("[,]");
        assertEquals("a", array[0]);
        assertEquals("b", array[1]);
        assertEquals("c", array[2]);
    }

    public void testGetParametersRandomWord() {
        command.addParameter(new Parameter("#A_WORD#"));
        String value = command.getParameter(0).stringValue();
        assertFalse("#A_WORD#".equals(value));
        assertTrue(value.length() > 0);
    }

    public void testGetParametersRandomAlpha() {
        command.addParameter(new Parameter("#AN_ALPHAWORD#"));
        String value = command.getParameter(0).stringValue();
        assertFalse("#AN_ALPHAWORD#".equals(value));
        assertTrue(value.length() > 0);
    }

    public void testGetParametersRandomString() {
        command.addParameter(new Parameter("#A_STRING#"));
        String value = command.getParameter(0).stringValue();
        assertFalse("#A_STRING#".equals(value));
        assertTrue(value.length() > 0);
    }

    public void testGetParametersRandomD100() {
        diceTest(100);
    }

    private void diceTest(int sides) {
        command.addParameter(new Parameter("#A_D" + sides + "#"));
        int number = command.getParameter(0).intValue();
        assertTrue(number > 0);
        assertTrue(number <= sides);
    }

    public void testGetParametersRandomD20() {
        diceTest(20);
    }

    public void testGetParametersRandomD12() {
        diceTest(12);
    }

    public void testGetParametersRandomD10() {
        diceTest(10);
    }

    public void testGetParametersRandomD8() {
        diceTest(8);
    }

    public void testGetParametersRandomD6() {
        diceTest(6);
    }

    public void testGetParametersRandomD4() {
        diceTest(4);
    }

    public void testGetParametersRandomByte() {
        command.addParameter(new Parameter("#A_BYTE#"));
        int number = command.getParameter(0).intValue();
        assertTrue(number >= Byte.MIN_VALUE);
        assertTrue(number <= Byte.MAX_VALUE);
    }

    public void testGetParametersRandomBoolean() {
        command.addParameter(new Parameter("#A_BOOLEAN#"));
        boolean bool = command.getParameter(0).booleanValue();
        assertTrue(bool || !bool);
    }

    public void testGetParametersRandomShort() {
        command.addParameter(new Parameter("#A_SHORT#"));
        int number = command.getParameter(0).intValue();
        assertTrue(number >= Short.MIN_VALUE);
        assertTrue(number <= Short.MAX_VALUE);
    }

    public void testGetParametersRandomInt() {
        command.addParameter(new Parameter("#AN_INT#"));
        int number = command.getParameter(0).intValue();
        assertTrue(number >= Integer.MIN_VALUE);
        assertTrue(number <= Integer.MAX_VALUE);
    }

    public void testGetParametersRandomFloat() {
        command.addParameter(new Parameter("#A_FLOAT#"));
        float number = command.getParameter(0).floatValue();
        assertTrue(number >= Float.MIN_VALUE);
        assertTrue(number <= Float.MAX_VALUE);
    }

    public void testGetParametersRandomLong() {
        command.addParameter(new Parameter("#A_LONG#"));
        long number = command.getParameter(0).longValue();
        assertTrue(number >= Long.MIN_VALUE);
        assertTrue(number <= Long.MAX_VALUE);
    }

    public void testGetParametersRandomDouble() {
        command.addParameter(new Parameter("#A_DOUBLE#"));
        double number = command.getParameter(0).doubleValue();
        assertTrue(number >= Double.MIN_VALUE);
        assertTrue(number <= Double.MAX_VALUE);
    }

    public void testCopyParameterFromNamedParameter() {
        parameter = new Parameter(new NamedParameter("string", "hello"));
        assertEquals("string=hello", parameter.stringValue());
    }

    public void testFailsCopyParameterFromNamedParameter() {
        try {
            parameter = new Parameter((NamedParameter) null);
            fail("IllegalArgumentException not thrown");
        } catch (RuntimeException e) {
            //
        }
    }

    public void testGetParametersFunction() {
        command.addParameter(new Parameter("{40+2}"));
        int number = command.getParameter(0).intValue();
        assertEquals(42, number);
    }

}

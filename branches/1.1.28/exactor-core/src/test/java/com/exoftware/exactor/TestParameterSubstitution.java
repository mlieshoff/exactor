package com.exoftware.exactor;

import junit.framework.TestCase;

/**
 * Test class for <code>Parameter</code>. Specifically test parameter substitution cases.
 *
 * @author Brian Swan
 */
public class TestParameterSubstitution extends TestCase {
    private SampleCommand command;
    private Parameter parameterOne;
    private Parameter parameterTwo;

    protected void setUp() throws Exception {
        command = new SampleCommand();
        command.getScript().getContext().put("key1", "Hello");
        command.getScript().getContext().put("key2", "World");
        parameterOne = new Parameter("[key1]");
        parameterOne.setCommand(command);
        parameterTwo = new Parameter("[key2]");
        parameterTwo.setCommand(command);
    }

    public void testSubstituteParameters() {
        assertEquals("Hello", parameterOne.stringValue());
        assertEquals("World", parameterTwo.stringValue());
    }

    public void testSubstituteParameters_MultipleTimes() {
        Parameter aParameter = makeTestParameter("[key1]");
        Parameter anotherParameter = makeTestParameter("[key1]");
        assertEquals("Hello", aParameter.stringValue());
        assertEquals("Hello", anotherParameter.stringValue());
    }

    public void testSubstituteParameters_MultipleTimesInOneParameter() {
        Parameter parameter = makeTestParameter("xxx[key1]xxx[key2]xxx");
        assertEquals("xxxHelloxxxWorldxxx", parameter.stringValue());
    }

    public void testSubstituteParameters_MultipleTimesInOneParameterOneNonExistent() {
        assertEquals("xxx[key999]xxxHelloxxx", makeTestParameter("xxx[key999]xxx[key1]xxx").stringValue());
        assertEquals("xxx[key999]Helloxxx", makeTestParameter("xxx[key999][key1]xxx").stringValue());
        assertEquals("xxxHello[key999]xxx", makeTestParameter("xxx[key1][key999]xxx").stringValue());
    }

    public void testSubstituteParameters_MultipleTimesNotInOrder() {
        Parameter parameter1 = makeTestParameter("[key1]");
        Parameter parameter2 = makeTestParameter("[key2]");
        Parameter parameter3 = makeTestParameter("[key1]");
        assertEquals("Hello", parameter1.stringValue());
        assertEquals("World", parameter2.stringValue());
        assertEquals("Hello", parameter3.stringValue());
    }

    public void testSubstituteWithNonExistantReplacement_Sean() {
        Parameter parameter = makeTestParameter("[does not exist]");
        assertEquals("[does not exist]", parameter.stringValue());
    }

    public void testSubstituteEmbeddedParams() {
        Parameter parameter = makeTestParameter("Hello [key2] Hello");
        assertEquals("Hello World Hello", parameter.stringValue());
    }

    public void testSubstitutionWithinSquareBrackets() {
        Parameter parameter = makeTestParameter("[expression=[key1]]");
        assertEquals("[expression=Hello]", parameter.stringValue());
    }

    private Parameter makeTestParameter(String paramValue) {
        Parameter aParameter = new Parameter(paramValue);
        aParameter.setCommand(command);
        return aParameter;
    }
}

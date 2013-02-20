/******************************************************************
 * Copyright (c) 2004, Exoftware
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *   * Neither the name of the Exoftware, Exactor nor the names
 *     of its contributors may be used to endorse or promote
 *     products derived from this software without specific
 *     prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************/
package com.exoftware.exactor;

import com.exoftware.exactor.command.annotated.NamedParameter;
import com.exoftware.util.RandomResolver;
import com.exoftware.util.Require;

import java.util.Map;

/**
 * A parameter to a command. Provides convenience methods to obtain the numeric
 * or boolean value of the parameter.
 *
 * @author Brian Swan
 */
public class Parameter {
    private static final String SUBSTITUTION_START = "[";
    private static final String SUBSTITUTION_END = "]";

    protected final String value;
    private Command command;

    /**
     * Create a new Parameter with the specified value. If <code>s</code>
     * represents an integer or double value the numeric equivalent can be
     * obtained through the convenience methods, <code>intValue</code> and
     * <code>doubleValue</code>. If <code>s</code> equals, ignoring case, the
     * string 'true' the boolean equivalent can be obtained through the
     * convenience method, <code>booleanValue</code>. In all cases the string
     * value of the parameter can be obtained via <code>stringValue</code> or
     * <code>toString</code>.
     *
     * @param s the value of the parameter.
     * @see #intValue()
     * @see #doubleValue()
     * @see #booleanValue()
     */
    public Parameter(String s) {
        value = s;
    }

    /**
     * Copies a named parameter into a normal parameter.
     *
     * @param namedParameter named parameter to copy.
     */
    public Parameter(NamedParameter namedParameter) {
        Require.condition(namedParameter != null);
        value = namedParameter.getName() + "=" + namedParameter.originalValue();
    }

    /**
     * Returns the string value of the parameter, with any substitutions
     * replaced. A substitution is a value enclosed in square brackets, e.g.
     * <code>[xxx]</code>, where <code>xxx</code> represents a key in either the
     * script or execution set context for the command owning this parameter.
     * For example, as the execution set context contains all of the System
     * properties, the following code would display <code>Windows XP</code> on a
     * Windows XP machine.;
     * <p/>
     * <pre>
     * <code>
     * Parameter p = new Parameter( "[os.name]" );
     * p.setCommand( myCommand );
     * System.out.println( p.stringValue() );
     * </code>
     * </pre>
     *
     * @return the string value of the parameter.
     */
    public String stringValue() {
        if (getCommand() == null) {
            return value;
        }
        String result = replaceSubstitutions(value, getCommand().getScript().getContext());
        result = replaceSubstitutions(result, getCommand().getScript().getExecutionSet().getContext());
        result = RandomResolver.resolveRandoms(result);
        return result;
    }

    /**
     * Returns a string representation of the paremeter.
     *
     * @return a string representation of the parameter.
     */
    public String toString() {
        return this.stringValue();
    }

    /**
     * Returns the string value splitted by regexp of the parameter, with any
     * substitutions replaced. A substitution is a value enclosed in square
     * brackets, e.g. <code>[xxx]</code>, where <code>xxx</code> represents a
     * key in either the script or execution set context for the command owning
     * this parameter. For example, as the execution set context contains all of
     * the System properties, the following code would display
     * <code>Windows XP</code> on a Windows XP machine.;
     * <p/>
     * <pre>
     * <code>
     * Parameter p = new Parameter("[os.name]");
     * p.setCommand(myCommand);
     * System.out.println(p.splittedString("[,]")); // comma separated
     * </code>
     * </pre>
     *
     * @return the string value of the parameter.
     */
    public String[] splittedString(String regexp) {
        return stringValue().split(regexp);
    }

    /**
     * Check whether the parameter value represents a number.
     *
     * @return <code>true</code> if the parameter represents a number, otherwise
     *         <code>false</code>.
     */
    public boolean isNumeric() {
        char[] chars = stringValue().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!Character.isDigit(chars[i]) && chars[i] != '.' && chars[i] != '-') {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the int value of the parameter. Behaves the same as
     * <code>Integer.parseInt</code>.
     *
     * @return the int value of the parameter.
     * @throws NumberFormatException if the parameter value is not a number.
     * @see Integer#parseInt(String)
     */
    public int intValue() throws NumberFormatException {
        int result;
        try {
            result = Integer.parseInt(stringValue());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("For input string: \"" + stringValue() + "\"");
        }
        return result;
    }

    /**
     * Returns the float value of the parameter. Behaves the same as
     * <code>Float.parseFloat</code>.
     *
     * @return the float value of the parameter.
     * @throws NumberFormatException if the parameter value is not a number.
     * @see Float#parseFloat(String)
     */
    public float floatValue() throws NumberFormatException {
        float result;
        try {
            result = Float.parseFloat(stringValue());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("For input string: \"" + stringValue() + "\"");
        }
        return result;
    }

    /**
     * Returns the long value of the parameter. Behaves the same as
     * <code>Long.parseLong</code>.
     *
     * @return the long value of the parameter.
     * @throws NumberFormatException if the parameter value is not a number.
     * @see Long#parseLong(String)
     */
    public long longValue() {
        try {
            return Long.parseLong(stringValue());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("For input string: \"" + stringValue() + "\"");
        }
    }

    /**
     * Returns the double value of the parameter. Behaves the same as
     * <code>Double.parseDouble</code>.
     *
     * @return the double value of the parameter.
     * @throws NumberFormatException if the parameter value is not a number.
     * @see Double#parseDouble(String)
     */
    public double doubleValue() throws NumberFormatException {
        double result;
        try {
            result = Double.parseDouble(stringValue());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("For input string: \"" + stringValue() + "\"");
        }
        return result;
    }

    /**
     * Returns the boolean value of the parameter. Behaves the same as
     * <code>Boolean.valueOf</code>.
     *
     * @return <code>true</code> if the parameter value is equal, ignoring case,
     *         to the String 'true', otherwise <code>false</code>.
     * @see Boolean#valueOf(String)
     */
    public boolean booleanValue() {
        return Boolean.valueOf(stringValue()).booleanValue();
    }

    /**
     * Set the owning command for this parameter.
     *
     * @param c the command tnat owns this parameter.
     * @throws RuntimeException if the supplied command is <code>null</code>.
     */
    public void setCommand(Command c) throws RuntimeException {
        if (c == null) {
            throw new RuntimeException("Command cannot be null");
        }
        command = c;
    }

    /**
     * Returns the command that owns this parameter.
     *
     * @return the command that owns this parameter.
     */
    public Command getCommand() {
        return command;
    }

    private String replaceSubstitutions(String s, Map map) {
        StringBuffer result = new StringBuffer(s);
        int subStart = result.indexOf(SUBSTITUTION_START);
        int subEnd = result.indexOf(SUBSTITUTION_END);
        while (subStart != -1 && subEnd != -1 & subEnd > subStart) {
            String key = result.substring(subStart + SUBSTITUTION_START.length(), subEnd);
            int startFrom = replaceKey(map, key, result, subStart, subEnd);
            subStart = result.indexOf(SUBSTITUTION_START, startFrom);
            subEnd = result.indexOf(SUBSTITUTION_END, subStart);
        }
        return result.toString();
    }

    private int replaceKey(Map map, String key, StringBuffer result, int subStart, int subEnd) {
        int restartReplacementFrom = 0;
        if (map.containsKey(key)) {
            result.replace(subStart, subEnd + SUBSTITUTION_END.length(), map.get(key).toString());
        } else {
            restartReplacementFrom = result.indexOf(SUBSTITUTION_START, subStart) + 1;
        }
        return restartReplacementFrom;
    }

}

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

import com.exoftware.util.FileUtilities;
import com.exoftware.util.Require;
import junit.framework.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for all commands executed by the framework.
 * The class provides access to the parameters of the command and the script
 * which contains the command.
 * The class extends <code>junit.framework.Assert</code> so all the assert methods
 * are available for use.
 * <p/>
 * Subclasses must implement the <code>execute</code> method.
 * If the command is responsible for performing a check then the <code>execute</code> method
 * should throw an <code>AssertionFailedError</code> in the event that the check was
 * unsuccessful, e.g.
 * <pre>
 * <code>
 *     public void execute() throws Exception
 *     {
 *         if( !"hello".equals( "goodbye" ) )
 *             throw new AssertionFailedError( "Values not equal" );
 *     }
 * </code>
 * </pre>
 * or using the convenience method,
 * <pre>
 * <code>
 *     public void execute() throws Exception
 *     {
 *         assertEquals( "hello", "goodbye" );
 *     }
 * </code>
 * </pre>
 *
 * @author Brian Swan
 */
public class Command extends Assert {
    private final List parameters = new ArrayList();
    private Script script = new Script();
    private int lineNumber = 0;
    private String name = "";
    private long endTime;
    private long startTime;

    /**
     * Add a parameter to the command.
     *
     * @param p the parameter to add.
     * @throws NullPointerException if the supplied parameter is <code>null</code>.
     */
    public void addParameter(Parameter p) throws NullPointerException {
        Require.condition(p != null, "Parameter cannot be null");
        p.setCommand(this);
        parameters.add(p);
    }

    /**
     * Check whether the command has any parameters.
     *
     * @return <code>true</code> if the command has parameters,
     *         otherwise <code>false</code>.
     */
    public boolean hasParameters() {
        return countParameters() > 0;
    }

    /**
     * Returns the number of parameters added to the command.
     *
     * @return the number of parameters added to the command.
     */
    public int countParameters() {
        return parameters.size();
    }

    /**
     * Returns the parameter at the specified index.
     *
     * @param index the index of the parameter to return.
     * @return the parameter at the specified index.
     * @throws IndexOutOfBoundsException if the command has no parameters, or
     *                                   if the index is out of range (index < 0 || index >= countParameters()).
     */
    public Parameter getParameter(int index) throws IndexOutOfBoundsException {
        Parameter result = null;
        try {
            result = (Parameter) parameters.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + parameters.size());
            // this is done for consistancy across JDK versions
        }
        return result;
    }

    /**
     * Returns the parameter at the specified index.
     *
     * @param index the index of the parameter to return.
     * @return the parameter at the specified index. If not present returns <code>new Parameter(defaultValue)</code>;
     */
    public Parameter getParameter(int index, String defaultValue) {
        Parameter result = null;
        try {
            result = getParameter(index);
        } catch (IndexOutOfBoundsException e) {
            return new Parameter(defaultValue);
        }
        return result;
    }

    /**
     * Returns all the parameters in this command.
     *
     * @return all the parameters in this command.
     */
    public Parameter[] getParameters() {
        return (Parameter[]) parameters.toArray(new Parameter[0]);
    }

    /**
     * Returns the script that owns this command.
     *
     * @return the script that owns this command.
     */
    public Script getScript() {
        return script;
    }

    /**
     * Set the owning script for this command.
     *
     * @param s the script that owns this command.
     * @throws NullPointerException if the supplied script is <code>null</code>.
     */
    public void setScript(Script s) throws NullPointerException {
        Require.condition(s != null, "Script cannot be null");
        script = s;
    }

    /**
     * Subclasses should implement this method to perform the actions
     * of their command.
     * If the command is responsible for performing a check then a
     * <code>AssertionFailedError</code> should be thrown in the
     * event that the check fails.
     * By default this method fails with a "Not implemented" message.
     *
     * @throws Exception if an error occurs.
     * @see junit.framework.AssertionFailedError
     */
    public void execute() throws Exception {
        fail("Not implemented");
    }

    /**
     * Returns the number of the line in the script
     * where this command is defined.
     *
     * @return the line number of the command in the script.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * set the line number of the command in the script.
     *
     * @param lineNumber the line number of this command.
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Returns the command name for this command.
     *
     * @return the command name for this command.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the command.
     *
     * @param name the name of the command.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Substitute the supplied parameters for any placeholder parameters in the command.
     * A placeholder parameter is a parameter with a value of the format <code>$n</code>,
     * where <code>n</code> is the index into the supplied substitutions array.
     * If <code>n</code> is not a valid index into the substitutions array then the
     * parameter is left unchanged.
     *
     * @param substitutions the array of parameters to use for substitutions.
     */
    public void substituteParameters(Parameter[] substitutions) throws RuntimeException {
        Require.condition(substitutions != null, "Substitutions cannot be null");
        for (int i = 0; i < countParameters(); i++) {
            substituteParameter(getParameter(i), substitutions, i);
        }
    }

    private void substituteParameter(Parameter p, Parameter[] substitutions, int currentIndex) {
        if (p.stringValue().startsWith("$")) {
            try {
                int index = Integer.parseInt(p.stringValue().substring(1));
                parameters.add(currentIndex, substitutions[index]);
                parameters.remove(currentIndex + 1);
            } catch (Exception ignored) {
                // ignore exceptions and leave params unchanged
            }
        }
    }

    /**
     * Return all parameters as single string for display purposes. Also does any required substitution.
     *
     * @return all parametes as a string
     */
    public String parametersAsString() {
        String result = "";
        for (Iterator iterator = parameters.iterator(); iterator.hasNext(); ) {
            Parameter parameter = (Parameter) iterator.next();
            result += parameter.toString() + " ";
        }
        return result.trim();
    }

    /**
     * Resolves a filename relative to actual file used to create the script file. Recognizes a files as
     * been relative if it begins with a '.'.
     *
     * @param fileName to be resolved
     * @return resolved file name
     */
    public String resolveFileRelativeToScriptFile(String fileName) {
        return FileUtilities.resolveLocation(fileName, new File(getScript().getAbsolutePath()).getParentFile()
                .getAbsolutePath());
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getExecutionTime() {
        return endTime - startTime;
    }

}

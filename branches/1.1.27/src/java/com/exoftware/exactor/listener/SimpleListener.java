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
package com.exoftware.exactor.listener;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.ExecutionSet;
import com.exoftware.exactor.ExecutionSetListener;
import com.exoftware.exactor.Script;
import com.exoftware.exactor.command.Composite;
import junit.framework.AssertionFailedError;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * A simple listener, outputs to a <code>PrintStream</code>. By default the listener outputs to
 * <code>System.out</code>.
 *
 * @author Brian Swan
 */
public class SimpleListener implements ExecutionSetListener {
    private static final String INDENT = "\t";

    protected int compositeLevel = 0;

    private final PrintWriter output;
    private final ExecutionSummary summary;
    private Script currentScript = new Script();

    /**
     * Create a new <code>SimpleListener</code> outputing to <code>System.out</code>.
     */
    public SimpleListener() {
        this(new OutputStreamWriter(System.out));
    }

    /**
     * Create a new <code>SimpleListener</code> outputing to the supplied <code>Writer</code>.
     *
     * @param output the writer to output to.
     */
    public SimpleListener(Writer output) {
        this(new PrintWriter(output, true), new ExecutionSummary());
    }

    public SimpleListener(Writer output, ExecutionSummary summary) {
        this.output = new PrintWriter(output, true);
        this.summary = summary;
    }

    public void executionSetStarted(ExecutionSet es) {
        summary.executionStarted();
        output("ExecutionSet started", 0);
    }

    public void executionSetEnded(ExecutionSet es) {
        outputNewLine();
        output("Scripts run: " + summary.getScriptsRunCount(), 0);
        output("Failures: " + summary.getFailureCount(), 0);
        output("Errors: " + summary.getErrorCount(), 0);
        outputNewLine();
        output("Duration: " + summary.getElapsedTimeSeconds() + "s", 0);
    }

    public void scriptStarted(Script s) {
        summary.scriptStarted(s);
        currentScript = s;
        output("started: " + s.getName(), 1);
    }

    public void scriptEnded(Script s) {
        output("ended: " + s.getName(), 1);
    }

    public void commandStarted(Command c) {
        if (c instanceof Composite)
            compositeLevel++;
    }

    public void commandEnded(Command c, Throwable t) {
        if (c instanceof Composite)
            compositeLevel--;

        if (compositeLevel == 0)
            outputCommandResult(c, t);
    }

    protected void outputCommandResult(Command c, Throwable t) {
        summary.commandEnded(c, t);
        if (t == null) {
            output("OK: " + formattedCommand(c), 2);
        } else if (t instanceof AssertionFailedError) {
            output("Failed: " + getErrorLocation(c), 2);
            output(t.getMessage(), 3);
        } else {
            output("Error: " + getErrorLocation(c), 2);
            output(t.getMessage(), 3);
            t.printStackTrace(output);
            outputNewLine();
        }
    }

    private String getErrorLocation(Command c) {
        return c.getName() + " (" + currentScript.getAbsolutePath() + ":" + c.getLineNumber()
                + ")";
    }

    private void outputNewLine() {
        output.println();
    }

    protected void output(String message, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            output.print(INDENT);
        }
        output.println(message);
    }

    private String formattedCommand(Command c) {
        return (c.getName() + " " + c.parametersAsString()).trim();
    }

    public boolean errorsOccured() {
        return (summary.getErrorCount() + summary.getFailureCount()) > 0;
    }

}

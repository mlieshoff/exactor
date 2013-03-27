/******************************************************************
 * Copyright (c) 2005, Exoftware
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

import junit.framework.AssertionFailedError;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Brian Swan
 */
public class LineSummary {
    private static final String NEW_LINE = System.getProperty("line.separator");

    private String line;
    private boolean passed = true;
    private String errorText = "";
    private long _executionTime = 0;

    public LineSummary(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    public boolean hasPassed() {
        return passed;
    }

    public String getErrorText() {
        return errorText;
    }

    public void commandEnded(long executionTime, Throwable throwable) {
        if (throwable instanceof AssertionFailedError) {
            errorText = throwable.getMessage();
        } else if (throwable != null) {
            errorText = throwable.getMessage() + NEW_LINE + stackTraceString(throwable);
        }
        passed = throwable == null;
        _executionTime = executionTime;
    }

    private String stackTraceString(Throwable throwable) {
        StringWriter w = new StringWriter();
        throwable.printStackTrace(new PrintWriter(w));
        w.flush();
        return w.getBuffer().toString();
    }

    public long getExecutionTime() {
        return _executionTime;
    }
}

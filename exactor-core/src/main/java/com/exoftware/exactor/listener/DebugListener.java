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

import com.exoftware.exactor.Command;

import java.io.Writer;

/**
 * Same behaviour as <code>SimpleListener</code> but doesn't filter out
 * events from composite commands.
 * <p/>
 * For example, the <code>DebugListener</code> run against the
 * documentation composite example gives the following output.
 * <p/>
 * <pre>
 * <code>
 * ExecutionSet started
 * 	    started: pagetitles.act
 * 		    	OK: SetBaseUrl
 * 			    OK: BeginAt
 * 			    OK: CheckTitleEquals
 * 		    OK: CheckPageTitle
 * 			    OK: SetBaseUrl
 * 			    OK: BeginAt
 * 			    OK: CheckTitleEquals
 * 		    OK: CheckPageTitle
 * 	    ended: pagetitles.act
 *
 * Scripts run: 1
 * Failures: 0
 * Errors: 0
 * </code>
 * </pre>
 *
 * @author Brian Swan
 * @see SimpleListener
 */
public class DebugListener extends SimpleListener {
    /**
     * Create a new <code>DebugListener</code> outputing to <code>System.out</code>.
     */
    public DebugListener() {
        super();
    }

    /**
     * Create a new <code>DebugListener</code> outputing to the supplied <code>Writer</code>.
     *
     * @param output the writer to output to.
     */
    public DebugListener(Writer output) {
        super(output);
    }

    public void commandEnded(Command c, Throwable t) {
        super.commandEnded(c, t);
        if (compositeLevel != 0) {
            outputCommandResult(c, t);
        }
    }

    protected void output(String message, int indentLevel) {
        super.output(message, indentLevel + compositeLevel);
    }
}

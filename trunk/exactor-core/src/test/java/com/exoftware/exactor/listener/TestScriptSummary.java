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

import java.io.File;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.MockCommand;

/**
 * @author Brian Swan
 */
public class TestScriptSummary extends TestCase
{
    private ScriptSummary summary;
    private MockCommand command;

    protected void setUp() throws Exception
    {
        summary = new ScriptSummary( new File( Constants.pathToTestFile( "single.act" ) ) );
        command = new MockCommand();
    }

    public void testCreate()
    {
        assertEquals( "single", summary.getName() );
        assertEquals( Constants.pathToTestFile( "single.act" ), summary.getAbsolutePath() );
        assertTrue( summary.hasPassed() );
        assertEquals( 1, summary.getLineSummaries().length );
        assertEquals( "MockCommand", summary.getLineSummaries()[0].getLine() );
    }

    public void testCommandEndedWithFailure() {
        command.setLineNumber(1);
        summary.commandEnded(command, new AssertionFailedError("A failure"));
        assertFalse(summary.hasPassed());
        assertFalse(summary.getLineSummaries()[0].hasPassed());
        assertEquals(0, summary.getExecutionTime());
    }

    public void testFailedCommandFollowedByPassingCommand()
    {
        summary = new ScriptSummary( new File( Constants.pathToTestFile( "declare.act" ) ) );
        command.setLineNumber( 1 );
        summary.commandEnded( command, new Exception( "An error" ) );
        assertFalse( summary.hasPassed() );
        command.setLineNumber( 2 );
        summary.commandEnded( command, null );
        assertFalse( summary.hasPassed() );
    }


}

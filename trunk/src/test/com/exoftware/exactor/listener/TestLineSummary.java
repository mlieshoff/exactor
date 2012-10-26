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

import com.exoftware.exactor.MockCommand;
import junit.framework.TestCase;
import junit.framework.AssertionFailedError;
import com.exoftware.exactor.Constants;

/**
 * @author Brian Swan
 */
public class TestLineSummary extends TestCase
{
    private LineSummary summary;

    protected void setUp() throws Exception
    {
        summary = new LineSummary( "A line" );
    }

    public void testCreate()
    {
        assertEquals( "A line", summary.getLine() );
        assertTrue( summary.hasPassed() );
        assertEquals( "", summary.getErrorText() );
    }

    public void testCommandEndedWithFailure()
    {
        summary.commandEnded( new MockCommand().getExecutionTime(), new AssertionFailedError( "Command Failed" ) );
        assertFalse( summary.hasPassed() );
        assertEquals( "Command Failed", summary.getErrorText() );
        assertEquals(0, summary.getExecutionTime());
    }

    public void testCommandEndedWithError()
    {
        summary.commandEnded( new MockCommand().getExecutionTime(), new MockException( "An error" ) );
        assertFalse( summary.hasPassed() );
        assertEquals( "An error" + Constants.NEW_LINE + "stacktrace", summary.getErrorText() );
        assertEquals(0, summary.getExecutionTime());
    }

    public void testCommandEndedSuccess()
    {
        summary.commandEnded( new MockCommand().getExecutionTime(), null );
        assertTrue( summary.hasPassed() );
        assertEquals( "", summary.getErrorText() );
        assertEquals(0, summary.getExecutionTime());
    }
}

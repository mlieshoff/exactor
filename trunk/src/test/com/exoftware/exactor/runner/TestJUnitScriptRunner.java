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
package com.exoftware.exactor.runner;

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.MockCommand;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.textui.TestRunner;

/**
 * Test class for <code>JUnitScriptRunner</code>.
 *
 * @author Sean Hanly
 */

public class TestJUnitScriptRunner extends TestCase
{
    protected void setUp() throws Exception
    {
        System.setProperty( "test.dir", Constants.DATA_DIR + "multiplefiles" );
        MockCommand.staticExecuteCalledCount = 0;
        System.setProperty( "ide", "false" );
    }

    public void testSuite()
    {
        Test test = JUnitScriptRunner.suite();
        assertEquals( 2, test.countTestCases() );
        System.setProperty( "ide", "true" );
    }

    public void testRunner()
    {
        System.setProperty( "test.dir", Constants.DATA_DIR + "multiplefiles" );
        TestRunner testRunner = new TestRunner();
        TestResult testResult = testRunner.doRun( JUnitScriptRunner.suite() );

        assertEquals( 2, testResult.runCount() );
        assertEquals( 4, MockCommand.staticExecuteCalledCount );
    }
}

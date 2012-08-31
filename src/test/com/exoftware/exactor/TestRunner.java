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
package com.exoftware.exactor;

import java.io.FileNotFoundException;

/**
 * @author Brian Swan
 */
public class TestRunner extends ExecutionSetListenerTestCase
{
    private Runner runner;

    public void testCreateWithNonExistantFile()
    {
        try
        {
            new Runner( "junk" );
            fail( "FileNotFoundException not thrown" );
        }
        catch( FileNotFoundException e )
        {
            assertEquals( "No such file or directory: junk", e.getMessage() );
        }
    }

    public void testRunWithEmptyFile() throws Exception
    {
        runner = new Runner( Constants.DATA_DIR + "empty.act" );
        runner.addListener( this );
        runner.run();
        assertEquals( 1, executionSetStartedCount );
        assertEquals( 1, executionStartedCount );
        assertEquals( 0, commandStartedCount );
        assertEquals( 0, commandEndedCount );
        assertEquals( 1, executionEndedCount );
        assertEquals( 1, executionSetEndedCount );
    }

    public void testRunWithSingleCommandFile() throws Exception
    {
        runner = new Runner( Constants.DATA_DIR + "single.act" );
        runner.addListener( this );
        runner.run();
        assertEquals( 1, executionSetStartedCount );
        assertEquals( 1, executionStartedCount );
        assertEquals( "single.act", scriptStartedParameter.getName() );
        assertEquals( 1, commandStartedCount );
        assertTrue( commandedStartedParameter instanceof MockCommand );
        assertEquals( 1, commandEndedCount );
        assertEquals( 1, executionEndedCount );
        assertEquals( 1, executionSetEndedCount );
    }

    public void testBaseDir() throws Exception
    {
        runner = new Runner( Constants.DATA_DIR );
        assertEquals( Constants.DATA_DIR, runner.getBaseDir() );
        runner = new Runner( Constants.DATA_DIR + "single.act" );
        assertEquals( Constants.DATA_DIR, runner.getBaseDir() );
    }
}

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
import java.io.StringWriter;

import junit.framework.TestCase;

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.MockCommand;
import com.exoftware.exactor.Script;
import com.exoftware.exactor.command.Composite;

/**
 * Test class for <code>DebugListener</code>.
 *
 * @author Brian Swan
 */
public class TestDebugListener extends TestCase
{
    private static final String NEW_LINE = System.getProperty( "line.separator" );
    private static final File SCRIPT_FILE = new File( Constants.DATA_DIR + "empty.act" );

    private StringWriter output;
    private SimpleListener listener;
    private MockCommand command;
    private Script script;

    protected void setUp() throws Exception
    {
        output = new StringWriter();
        listener = new DebugListener( output );
        script = new Script( SCRIPT_FILE );
        command = new MockCommand();
        command.setName( "TestCommand" );
        command.setLineNumber( 1 );
    }

    public void testExecutionSetWithComposite()
    {
        script.addCommand( command );
        Composite composite = new Composite( script );
        composite.setName( "Composite" );
        listener.commandStarted( composite );
        listener.commandStarted( command );
        listener.commandEnded( command, null );
        listener.commandEnded( composite, null );
        assertEquals( "\t\t\tOK: TestCommand" + NEW_LINE +
                "\t\tOK: Composite" + NEW_LINE, output.toString() );
    }
}

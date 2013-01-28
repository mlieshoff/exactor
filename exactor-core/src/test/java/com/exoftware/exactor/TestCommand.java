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

import java.io.File;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * Test class for <code>Command</code>.
 *
 * @author Brian Swan
 */
public class TestCommand extends TestCase
{
    private Command command;
    private Parameter parameter;

    protected void setUp() throws Exception
    {
        command = new Command();
        parameter = new Parameter( "hello" );
    }

    public void testCreateCommand()
    {
        assertFalse( command.hasParameters() );
        assertEquals( 0, command.countParameters() );
    }

    public void testAddParameter()
    {
        command.addParameter( parameter );
        assertTrue( command.hasParameters() );
        assertEquals( 1, command.countParameters() );
        assertEquals( "hello", command.getParameter( 0 ).stringValue() );
        assertSame( command, parameter.getCommand() );
    }

    public void testAddNullParameter()
    {
        try
        {
            command.addParameter( null );
            fail( "RuntimeException not thrown" );
        }
        catch ( RuntimeException e )
        {
            assertEquals( "Parameter cannot be null", e.getMessage() );
        }
    }

    public void testGetParameterWithNoneAdded()
    {
        assertFalse( command.hasParameters() );
        try
        {
            command.getParameter( 0 );
            fail( "IntdexOutOfBoundsException not thrown" );
        }
        catch ( IndexOutOfBoundsException e )
        {
            assertEquals( "Index: 0, Size: 0", e.getMessage() );
        }
    }

    public void testGetParameterWithInvalidIndex()
    {
        command.addParameter( parameter );
        try
        {
            command.getParameter( -1 );
            fail( "IndexOutOfBoundsException not thrown" );
        }
        catch ( IndexOutOfBoundsException e )
        {
            assertEquals( "Index: -1, Size: 1", e.getMessage() );
        }

        try
        {
            command.getParameter( 2 );
            fail( "IndexOutOfBoundsException not thrown" );
        }
        catch ( IndexOutOfBoundsException e )
        {
            assertEquals( "Index: 2, Size: 1", e.getMessage() );
        }
    }

    public void testSetScript()
    {
        Script s = new Script();
        command.setScript( s );
        assertSame( s, command.getScript() );
    }

    public void testSetNullScript()
    {
        try
        {
            command.setScript( null );
            fail( "RuntimeException not thrown" );
        }
        catch ( RuntimeException e )
        {
            assertEquals( "Script cannot be null", e.getMessage() );
        }
    }

    public void testGetUnSetScript()
    {
        Script s = command.getScript();
        assertNotNull( s );
        assertEquals( "DEFAULT", s.getName() );
    }

    public void testDefaultExecuteBehaviour() throws Exception
    {
        try
        {
            command.execute();
            fail( "AssertionFailedError not thrown" );
        }
        catch ( AssertionFailedError e )
        {
            assertEquals( "Not implemented", e.getMessage() );
        }
    }

    public void testSubstituteParameters()
    {
        command.addParameter( new Parameter( "$0" ) );
        command.addParameter( new Parameter( "$1" ) );
        command.substituteParameters( new Parameter[]{new Parameter( "Hello" ), new Parameter( "World" )} );
        assertEquals( "Hello", command.getParameter( 0 ).stringValue() );
        assertEquals( "World", command.getParameter( 1 ).stringValue() );
    }

    public void testSubstituteWithNullParameters()
    {
        try
        {
            command.substituteParameters( null );
            fail( "RuntimeException not thrown" );
        }
        catch ( RuntimeException e )
        {
            assertEquals( "Substitutions cannot be null", e.getMessage() );
        }
    }

    public void testSubstituteWithNonExistantReplacement()
    {
        command.addParameter( new Parameter( "$3" ) );
        command.substituteParameters( new Parameter[0] );
        assertEquals( "$3", command.getParameter( 0 ).stringValue() );
    }

    public void testSubstituteInvailidReplacementMarker()
    {
        command.addParameter( new Parameter( "$junk" ) );
        command.substituteParameters( new Parameter[0] );
        assertEquals( "$junk", command.getParameter( 0 ).stringValue() );
    }

    public void testSubstituteOutOfSequenceReplacement()
    {
        command.addParameter( new Parameter( "$1" ) );
        command.substituteParameters( new Parameter[]{new Parameter( "Hello" ), new Parameter( "World" )} );
        assertEquals( "World", command.getParameter( 0 ).stringValue() );
    }

    public void testGetParameters()
    {
        command.addParameter( new Parameter( "Hello" ) );
        command.addParameter( new Parameter( "World" ) );
        assertEquals( 2, command.getParameters().length );
        assertEquals( "Hello", command.getParameters()[0].stringValue() );
        assertEquals( "World", command.getParameters()[1].stringValue() );
    }

    public void testParametersAsString()
    {
        command.addParameter( new Parameter( "Hello" ) );
        command.addParameter( new Parameter( "World" ) );

        assertEquals( "Hello World", command.parametersAsString() );
    }

    public void testResolveFile_Relative()
    {
       command.setScript( new Script( new File( Constants.pathToTestFile( "empty.act" ) ) ) );

       assertEquals( System.getProperty( "user.dir" ) + File.separator + "src" + File.separator + "test"
               + File.separator + "resources/data" + File.separator +  "./afile.txt",
               command.resolveFileRelativeToScriptFile("./afile.txt") );
    }

    public void testResolveFile_AbsoluteRelative()
    {
       command.setScript( new Script( new File( Constants.pathToTestFile( "empty.act" ) ) ) );

       assertEquals( "afile.txt",
                     command.resolveFileRelativeToScriptFile("afile.txt") );
    }

    public void testGetParametersDefault( )
    {
       command.addParameter( new Parameter( "Hello" ) );
       command.addParameter( new Parameter( "World" ) );
       assertEquals( "Hello", command.getParameter( 0, "DEFAULT_VALUE" ).stringValue( ) );
       assertEquals( "World", command.getParameter( 1, "DEFAULT_VALUE" ).stringValue( ) );

       assertEquals( "DEFAULT_VALUE", command.getParameter( 2, "DEFAULT_VALUE" ).stringValue( ) );
    }
}

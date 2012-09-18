package com.exoftware.exactor.parser;

import com.exoftware.exactor.*;
import com.exoftware.exactor.command.Composite;
import com.exoftware.exactor.command.Unknown;
import junit.framework.TestCase;

import java.io.File;

/**
 * Test class for <code>ScriptParser</code>.
 *
 * @author Brian Swan
 */
public class TestScriptParser extends TestCase
{
    private ScriptParser scriptParser;
    private File file;

    protected void setUp() throws Exception
    {
        scriptParser = new ScriptParser( new ExecutionSet() );
    }

    public void testCreateWithNullExecutionSet() throws Exception
    {
        try
        {
            new ScriptParser( null );
            fail( "RuntimeException not thrown" );
        }
        catch( RuntimeException e )
        {
            assertEquals( "ExecutionSet cannot be null", e.getMessage() );
        }
    }

    public void testParseNullFile() throws Exception
    {
        try
        {
            scriptParser.parse( null );
            fail( "RuntimeException not thrown" );
        }
        catch( RuntimeException e )
        {
            assertEquals( "Cannot parse null file", e.getMessage() );
        }
    }

    public void testParseEmptyFile() throws Exception
    {
        file = new File( Constants.DATA_DIR + "empty.act" );
        Script script = scriptParser.parse( file );
        assertNotNull( script );
        assertEquals( file.getName(), script.getName() );
        assertEquals( 0, script.countCommands() );
    }

    public void testParseSingleCommandFile() throws Exception
    {
        file = new File( Constants.DATA_DIR + "single.act" );
        Script script = scriptParser.parse( file );
        assertNotNull( script );
        assertEquals( 1, script.countCommands() );
        Command command = script.getCommand( 0 );
        assertTrue( command instanceof MockCommand );
        assertEquals( 1, command.getLineNumber() );
        assertEquals( "MockCommand", command.getName() );
    }

    public void testParseSingleCommandWithParameters() throws Exception
    {
        file = new File( Constants.DATA_DIR + "singlewithparams.act" );
        Script script = scriptParser.parse( file );
        assertNotNull( script );
        assertEquals( 1, script.countCommands() );
        Command command = script.getCommand( 0 );
        assertEquals( 3, command.countParameters() );
        assertEquals( "test", command.getParameter( 0 ).stringValue() );
        assertEquals( 123, command.getParameter( 1 ).intValue() );
        assertEquals( true, command.getParameter( 2 ).booleanValue() );
    }

    public void testParseFileWithComments() throws Exception
    {
        file = new File( Constants.DATA_DIR + "comments.act" );
        Script script = scriptParser.parse( file );
        assertNotNull( script );
        assertEquals( 1, script.countCommands() );
        assertTrue( script.getCommand( 0 ) instanceof MockCommand );
    }

    public void testParseFileWithUnknownCommand() throws Exception
    {
        file = new File( Constants.DATA_DIR + "unknown.act" );
        Script script = scriptParser.parse( file );
        assertNotNull( script );
        assertEquals( 1, script.countCommands() );
        Command command = script.getCommand( 0 );
        assertTrue( command instanceof Unknown );
        assertEquals( "AnUnknownCommand", command.getName() );
    }

    public void testParseFileWithQuotedParameters() throws Exception
    {
        file = new File( Constants.DATA_DIR + "quotedparams.act" );
        Script script = scriptParser.parse( file );
        assertNotNull( script );
        assertEquals( 1, script.countCommands() );
        Command command = script.getCommand( 0 );
        assertEquals( 3, command.countParameters() );
        assertEquals( "first parameter", command.getParameter( 0 ).stringValue() );
        assertEquals( 123, command.getParameter( 1 ).intValue() );
        assertEquals( "another parameter", command.getParameter( 2 ).stringValue() );
    }

    public void testParseFileWithCompositeCommand() throws Exception
    {
        file = new File( Constants.DATA_DIR + "composite.act" );
        Script script = scriptParser.parse( file );
        assertNotNull( script );
        assertEquals( 3, script.countCommands() );
        assertTrue( script.getCommand( 0 ) instanceof Composite );
        assertEquals( "AComposite", script.getCommand( 0 ).getName() );
        assertTrue( script.getCommand( 1 ) instanceof MockCommand );
        assertTrue( script.getCommand( 2 ) instanceof Composite );
        assertEquals( 2, script.getCommand( 0 ).countParameters() );
    }

    public void testParseFileWithBlankLines() throws Exception
    {
        file = new File( Constants.DATA_DIR + "blanklines.act" );
        Script script = scriptParser.parse( file );
        assertNotNull( script );
        assertEquals( 2, script.countCommands() );

        assertEquals( 2, script.getCommand( 0 ).getLineNumber() );
        assertEquals( 4, script.getCommand( 1 ).getLineNumber() );
    }
}

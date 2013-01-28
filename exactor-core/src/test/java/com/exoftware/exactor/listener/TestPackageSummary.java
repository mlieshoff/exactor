package com.exoftware.exactor.listener;

import java.io.File;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.MockCommand;
import com.exoftware.exactor.Script;

public class TestPackageSummary extends TestCase
{
    private PackageSummary summary;
    private Script script;

    protected void setUp() throws Exception
    {
        summary = new PackageSummary( "mytests" );
        script = new Script( new File( Constants.pathToTestFile( "single.act" ) ) );
    }

    public void testCreate()
    {
        assertEquals( "mytests", summary.getPackageName() );
        assertSummary( summary, 0, 0, 0 );
        assertTrue( summary.hasPassed() );
        assertEquals( 0, summary.getScriptSummaries().length );
    }

    public void testGetScriptSummaries()
    {
        assertEquals( 0, summary.getScriptSummaries().length );
        summary.scriptStarted( script );
        assertEquals( 1, summary.getScriptSummaries().length );
    }

    public void testCommandEndedWithFailure()
    {
        MockCommand command = new MockCommand();
        command.setLineNumber( 1 );
        summary.scriptStarted( script );
        summary.commandEnded( command, new AssertionFailedError( "A failure" ) );
        assertFalse( summary.hasPassed() );
        assertSummary( summary, 1, 1, 0 );
        assertFalse( summary.getScriptSummaries()[0].hasPassed() );
    }

    public void testCommandEndedWithError()
    {
        MockCommand command = new MockCommand();
        command.setLineNumber( 1 );
        summary.scriptStarted( script );
        summary.commandEnded( command, new Exception( "An error" ) );
        assertFalse( summary.hasPassed() );
        assertSummary( summary, 1, 0, 1 );
        assertFalse( summary.getScriptSummaries()[0].hasPassed() );
    }

    private void assertSummary( PackageSummary summary, int expectedRuns, int expectedFailures, int expectedErrors )
    {
        assertEquals( "Incorrect run count", expectedRuns, summary.getScriptsRunCount() );
        assertEquals( "Incorrect failure count", expectedFailures, summary.getFailureCount() );
        assertEquals( "Incorrect error count", expectedErrors, summary.getErrorCount() );
    }

}

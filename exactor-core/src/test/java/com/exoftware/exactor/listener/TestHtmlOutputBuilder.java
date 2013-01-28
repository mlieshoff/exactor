package com.exoftware.exactor.listener;

import java.io.File;

import junit.framework.TestCase;

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.MockCommand;
import com.exoftware.exactor.Script;

public class TestHtmlOutputBuilder extends TestCase
{
    private static final String NL = System.getProperty( "line.separator" );
    private HtmlOutputBuilder builder;

    protected void setUp() throws Exception
    {
        builder = new HtmlOutputBuilder();
    }

    public void testBuildHtmlHeader()
    {
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" + NL +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" + NL +
                "<head>" + NL +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\"/>" + NL +
                "<title>Acceptance Test Results</title>" + NL +
                "<style type=\"text/css\" media=\"all\">@import \"style.css\";</style>" + NL +
                "</head>" + NL +
                "<body>" + NL;
        assertEquals( expected, builder.buildHtmlHeader() );
    }

    public void testBuildHtmlFooter()
    {
        String expected = "</body>" + NL +
                "</html>";
        assertEquals( expected, builder.buildHtmlFooter() );
    }

    public void testBuildSummaryTable()
    {
        String expected = "<h2>Summary</h2>" + NL +
                "<table>" + NL +
                "<tr><th>Run</th><th>Failures</th><th>Errors</th><th>Time</th><th>Test time in ms</th></tr>" + NL +
                "<tr><td>0</td><td>0</td><td>0</td><td>0s</td><td class=\"Time\">0</td></tr>" + NL +
                "</table>" + NL;
        ExecutionSummary executionSummary = new ExecutionSummary()
        {
            protected long getCurrentTime()
            {
                return 0;
            }
        };
        assertEquals( expected, builder.buildSummaryTable( executionSummary ) );
    }

    public void testBuildPassingPackageSummaryRow()
    {
        PackageSummary summary = new PackageSummary( "MyTests" );
        String expected = "<tr><td><a href=\"#MyTests\">MyTests</a></td><td>0</td><td>0</td><td>0</td><td class=\"Pass\">Pass</td><td class=\"Time\">0</td></tr>" + NL;
        assertEquals( expected, builder.buildPackageSummaryRow( summary ) );
    }

    public void testBuildFailingPackageSummaryRow()
    {
        PackageSummary summary = new PackageSummary( "multiplefiles" );
        Script s = new Script( new File( Constants.pathToTestFile( "multiplefiles" + Constants.FS + "test1.act" ) ) );
        MockCommand command = new MockCommand();
        summary.scriptStarted( s );
        summary.commandEnded( command, new Exception( "An Error" ) );
        String expected = "<tr><td><a href=\"#multiplefiles\">multiplefiles</a></td><td>1</td><td>0</td><td>1</td><td class=\"Fail\">Fail</td><td class=\"Time\">0</td></tr>" + NL;
        assertEquals( expected, builder.buildPackageSummaryRow( summary ) );
    }

    public void testBuildPackageSummaryTable()
    {
        PackageSummary[] summaries = new PackageSummary[]{new PackageSummary( "MyTests" )};
        String expected = "<h2>Packages</h2>" + NL +
                "<table>" + NL +
                "<tr><th>Name</th><th>Run</th><th>Failures</th><th>Errors</th><th>Result</th><th>Time in ms</th></tr>" + NL +
                "<tr><td><a href=\"#MyTests\">MyTests</a></td><td>0</td><td>0</td><td>0</td><td class=\"Pass\">Pass</td><td class=\"Time\">0</td></tr>" + NL +
                "</table>" + NL;
        assertEquals( expected, builder.buildPackageSummaryTable( summaries ) );
    }

    public void testBuildPassingScriptSummaryRow()
    {
        ScriptSummary summary = new ScriptSummary( new File( Constants.pathToTestFile( "single.act" ) ) );
        String expected = "<tr><td><a href=\"#single\">single</a></td><td class=\"Pass\">Pass</td><td class=\"Time\">0</td></tr>" + NL;
        assertEquals( expected, builder.buildScriptSummaryRow( summary ) );
    }

    public void testBuildFailingScriptSummaryRow()
    {
        ScriptSummary summary = new ScriptSummary( new File( Constants.pathToTestFile( "single.act" ) ) );
        summary.commandEnded( new MockCommand(), new Exception( "An error" ) );
        String expected = "<tr><td><a href=\"#single\">single</a></td><td class=\"Fail\">Fail</td><td class=\"Time\">0</td></tr>" + NL;
        assertEquals( expected, builder.buildScriptSummaryRow( summary ) );
    }

    public void testBuildScriptSummaryTable()
    {
        PackageSummary summary = new PackageSummary( "MyTests" );
        summary.scriptStarted( new Script( new File( Constants.pathToTestFile( "single.act" ) ) ) );
        summary.commandEnded( new MockCommand(), null );
        String expected = "<h2 id=\"MyTests\">MyTests</h2>" + NL +
                "<table>" + NL +
                "<tr><th>Name</th><th>Result</th><th>Time in ms</th></tr>" + NL +
                "<tr><td><a href=\"#single\">single</a></td><td class=\"Pass\">Pass</td><td class=\"Time\">0</td></tr>" + NL +
                "</table>" + NL;
        assertEquals( expected, builder.buildScriptSummaryTable( summary ) );
    }

    public void testBuildScriptSummaryTables()
    {
        PackageSummary summary1 = new PackageSummary( "MyTests" );
        PackageSummary summary2 = new PackageSummary( "MyOtherTests" );
        PackageSummary[] summaries = new PackageSummary[]{summary1, summary2};
        summary1.scriptStarted( new Script( new File( Constants.pathToTestFile( "single.act" ) ) ) );
        summary1.commandEnded( new MockCommand(), null );
        summary2.scriptStarted( new Script( new File( Constants.pathToTestFile( "single.act" ) ) ) );
        summary2.commandEnded( new MockCommand(), new MockException( "An Error" ) );
        String expected = "<h2 id=\"MyTests\">MyTests</h2>" + NL +
                "<table>" + NL +
                "<tr><th>Name</th><th>Result</th><th>Time in ms</th></tr>" + NL +
                "<tr><td><a href=\"#single\">single</a></td><td class=\"Pass\">Pass</td><td class=\"Time\">0</td></tr>" + NL +
                "</table>" + NL +
                "<h2 id=\"MyOtherTests\">MyOtherTests</h2>" + NL +
                "<table>" + NL +
                "<tr><th>Name</th><th>Result</th><th>Time in ms</th></tr>" + NL +
                "<tr><td><a href=\"#single\">single</a></td><td class=\"Fail\">Fail</td><td class=\"Time\">0</td></tr>" + NL +
                "</table>" + NL;
        assertEquals( expected, builder.buildScriptSummaryTables( summaries ) );
    }

    public void testBuildPassingLineSummaryRow()
    {
        LineSummary summary = new LineSummary( "MockCommand 1  2" );
        String expected = "<tr><td>1</td><td class=\"LinePass\">MockCommand 1  2</td><td>OK</td><td class=\"Time\">0</td></tr>" + NL;
        assertEquals( expected, builder.buildLineSummaryRow( 1, summary ) );
    }

    public void testBuildFailingLineSummaryRow()
    {
        LineSummary summary = new LineSummary( "MockCommand 1  2" );
        summary.commandEnded( new MockCommand().getExecutionTime(), new MockException( "An Error" ) );
        String expected = "<tr><td>1</td><td class=\"LineFail\">MockCommand 1  2</td><td>An Error" + NL + "stacktrace</td><td class=\"Time\">0</td></tr>" + NL;
        assertEquals( expected, builder.buildLineSummaryRow( 1, summary ) );
    }

    public void testBuildBlankLineSummaryRow()
    {
        LineSummary summary = new LineSummary( "" );
        String expected = "<tr><td>1</td><td class=\"LinePass\">&nbsp;</td><td>OK</td><td class=\"Time\">0</td></tr>" + NL;
        assertEquals( expected, builder.buildLineSummaryRow( 1, summary ) );
    }

    public void testBuildLineSummaryTable()
    {
        ScriptSummary summary = new ScriptSummary( new File( Constants.pathToTestFile( "single.act" ) ) );
        String expected = "<h2 id=\"single\">single</h2>" + NL +
                "<table>" + NL +
                "<tr><th>Line</th><th>Command</th><th>Result</th><th>Time in ms</th></tr>" + NL +
                "<tr><td>0</td><td class=\"LinePass\">MockCommand</td><td>OK</td><td class=\"Time\">0</td></tr>" + NL +
                "</table>" + NL;
        assertEquals( expected, builder.buildLineSummaryTable( summary ) );
    }

    public void testBuildLineSummaryTables()
    {
        PackageSummary summary = new PackageSummary( "MyTests" );
        PackageSummary[] summaries = new PackageSummary[]{summary};
        Script script = new Script( new File( Constants.pathToTestFile( "single.act" ) ) );
        MockCommand command = new MockCommand();
        command.setLineNumber( 1 );
        summary.scriptStarted( script );
        summary.commandEnded( command, null );
        summary.scriptStarted( script );
        summary.commandEnded( command, new MockException( "An Error" ) );
        String expected = "<h2 id=\"single\">single</h2>" + NL +
                "<table>" + NL +
                "<tr><th>Line</th><th>Command</th><th>Result</th><th>Time in ms</th></tr>" + NL +
                "<tr><td>0</td><td class=\"LinePass\">MockCommand</td><td>OK</td><td class=\"Time\">0</td></tr>" + NL +
                "</table>" + NL +
                "<h2 id=\"single\">single</h2>" + NL +
                "<table>" + NL +
                "<tr><th>Line</th><th>Command</th><th>Result</th><th>Time in ms</th></tr>" + NL +
                "<tr><td>0</td><td class=\"LineFail\">MockCommand</td><td>An Error" + NL + "stacktrace</td><td class=\"Time\">0</td></tr>" + NL +
                "</table>" + NL;
        assertEquals( expected, builder.buildLineSummaryTables( summaries ) );
    }

}

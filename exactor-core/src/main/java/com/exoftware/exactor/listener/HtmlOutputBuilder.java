package com.exoftware.exactor.listener;

import java.text.MessageFormat;

public class HtmlOutputBuilder
{
    private static final String NL = System.getProperty( "line.separator" );
    private static final String PASS_VALUE = "Pass";
    private static final String FAIL_VALUE = "Fail";
    private static final String PASS_LINE_VALUE = "LinePass";
    private static final String FAIL_LINE_VALUE = "LineFail";
    private static final String RESULT_OK = "OK";
    private static final String NBSP = "&nbsp;";
    private static final String TABLE_HEADER = "<table>" + NL;
    private static final String TABLE_FOOTER = "</table>" + NL;
    private static final String SUMMARY_TABLE = "<h2>Summary</h2>" + NL +
            TABLE_HEADER +
            "<tr><th>Run</th><th>Failures</th><th>Errors</th><th>Time</th><th>Test time in ms</th></tr>" + NL +
            "<tr><td>{0}</td><td>{1}</td><td>{2}</td><td>{3}s</td><td class=\"Time\">{4}</td></tr>" + NL +
            TABLE_FOOTER;
    private static final String PACKAGE_SUMMARY_ROW = "<tr><td><a href=\"#{0}\">{0}</a></td><td>{1}</td><td>{2}</td><td>{3}</td><td class=\"{4}\">{4}</td><td class=\"Time\">{5}</td></tr>" + NL;
    private static final String PACKAGE_SUMMARY_TABLE_HEADER = "<h2>Packages</h2>" + NL +
            TABLE_HEADER +
            "<tr><th>Name</th><th>Run</th><th>Failures</th><th>Errors</th><th>Result</th><th>Time in ms</th></tr>" + NL;
    private static final String SCRIPT_SUMMARY_ROW = "<tr><td><a href=\"#{0}\">{0}</a></td><td class=\"{1}\">{1}</td><td class=\"Time\">{2}</td></tr>" + NL;
    private static final String SCRIPT_SUMMARY_TABLE_HEADER = "<h2 id=\"{0}\">{0}</h2>" + NL +
            TABLE_HEADER +
            "<tr><th>Name</th><th>Result</th><th>Time in ms</th></tr>" + NL;
    private static final String LINE_SUMMARY_ROW = "<tr><td>{0}</td><td class=\"{1}\">{2}</td><td>{3}</td><td class=\"Time\">{4}</td></tr>" + NL;
    private static final String LINE_SUMMARY_TABLE_HEADER = "<h2 id=\"{0}\">{0}</h2>" + NL +
            TABLE_HEADER +
            "<tr><th>Line</th><th>Command</th><th>Result</th><th>Time in ms</th></tr>" + NL;
    private static final String HTML_HEADER = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" + NL +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">" + NL +
            "<head>" + NL +
            "<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\"/>" + NL +
            "<title>Acceptance Test Results</title>" + NL +
            "<style type=\"text/css\" media=\"all\">@import \"style.css\";</style>" + NL +
            "</head>" + NL +
            "<body>" + NL;
    private static final String HTML_FOOTER = "</body>" + NL +
            "</html>";


    public String buildSummaryTable(ExecutionSummary executionSummary) {
        Object[] data = new Object[5];
        data[0] = new Integer(executionSummary.getScriptsRunCount());
        data[1] = new Integer(executionSummary.getFailureCount());
        data[2] = new Integer(executionSummary.getErrorCount());
        data[3] = new Long(executionSummary.getElapsedTimeSeconds());
        data[4] = time(executionSummary.getExecutionTime());
        return MessageFormat.format(SUMMARY_TABLE, data);
    }

    public String buildPackageSummaryRow(PackageSummary summary) {
        Object[] data = new Object[6];
        data[0] = summary.getPackageName();
        data[1] = new Integer(summary.getScriptsRunCount());
        data[2] = new Integer(summary.getFailureCount());
        data[3] = new Integer(summary.getErrorCount());
        data[4] = passValue(summary.hasPassed());
        data[5] = time(summary.getExecutionTime());
        return MessageFormat.format(PACKAGE_SUMMARY_ROW, data);
    }

    public String buildPackageSummaryTable( PackageSummary[] summaries )
    {
        StringBuffer result = new StringBuffer( PACKAGE_SUMMARY_TABLE_HEADER );
        for( int i = 0; i < summaries.length; i++ )
            result.append( buildPackageSummaryRow( summaries[i] ) );

        result.append( TABLE_FOOTER );
        return result.toString();
    }

    public String buildScriptSummaryRow(ScriptSummary summary) {
        Object[] data = new Object[3];
        data[0] = summary.getName();
        data[1] = passValue( summary.hasPassed() );
        data[2] = time(summary.getExecutionTime());
        return MessageFormat.format(SCRIPT_SUMMARY_ROW, data);
    }

    public String buildScriptSummaryTable( PackageSummary summary )
    {
        String[] data = new String[]{summary.getPackageName()};
        String header = MessageFormat.format( SCRIPT_SUMMARY_TABLE_HEADER, data );
        StringBuffer result = new StringBuffer( header );
        for( int i = 0; i < summary.getScriptSummaries().length; i++ )
            result.append( buildScriptSummaryRow( summary.getScriptSummaries()[i] ) );

        result.append( TABLE_FOOTER );
        return result.toString();
    }

    public String buildLineSummaryRow(int lineNumber, LineSummary summary) {
        Object[] data = new Object[5];
        data[0] = new Integer(lineNumber);
        data[1] = passValue(summary.getErrorText());
        data[2] = lineText(summary.getLine());
        data[3] = resultText(summary.getErrorText());
        data[4] = time(summary.getExecutionTime());
        return MessageFormat.format( LINE_SUMMARY_ROW, data );
    }

    private String lineText( String line )
    {
        return line.length() == 0 ? NBSP : line;
    }

    private String resultText( String errorText )
    {
        return errorText.length() == 0 ? RESULT_OK : errorText;
    }

    private String time(long time) {
        return String.valueOf(time);
    }

    private String passValue( boolean b )
    {
        return b ? PASS_VALUE : FAIL_VALUE;
    }

    private String passValue( String s )
    {
        return s.length() == 0 ? PASS_LINE_VALUE : FAIL_LINE_VALUE;
    }

    public String buildLineSummaryTable( ScriptSummary summary )
    {
        String[] data = new String[]{summary.getName()};
        String header = MessageFormat.format( LINE_SUMMARY_TABLE_HEADER, data );
        StringBuffer result = new StringBuffer( header );
        for( int i = 0; i < summary.getLineSummaries().length; i++ )
            result.append( buildLineSummaryRow( i, summary.getLineSummaries()[i] ) );

        result.append( TABLE_FOOTER );
        return result.toString();
    }

    public String buildHtmlHeader()
    {
        return HTML_HEADER;
    }

    public String buildHtmlFooter()
    {
        return HTML_FOOTER;
    }

    public String buildScriptSummaryTables( PackageSummary[] summaries )
    {
        StringBuffer result = new StringBuffer();
        for( int i = 0; i < summaries.length; i++ )
            result.append( buildScriptSummaryTable( summaries[i] ) );

        return result.toString();
    }

    public String buildLineSummaryTables( PackageSummary[] summaries )
    {
        StringBuffer result = new StringBuffer();
        for( int i = 0; i < summaries.length; i++ )
        {
            PackageSummary packageSummary = summaries[i];
            for( int j = 0; j < packageSummary.getScriptSummaries().length; j++ )
                result.append( buildLineSummaryTable( packageSummary.getScriptSummaries()[j] ) );
        }
        return result.toString();
    }
}

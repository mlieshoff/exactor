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

import com.exoftware.exactor.Command;
import com.exoftware.exactor.ExecutionSet;
import com.exoftware.exactor.ExecutionSetListener;
import com.exoftware.exactor.Script;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

/**
 * @author Brian Swan
 */
public class HtmlOutputListener implements ExecutionSetListener {
    private static final String DEFAULT_PACKAGE_BASE = System.getProperty("user.dir");
    private static final String DEFAULT_HTML = "out.html";
    private static final String DEFAULT_STYLE_SHEET = "style.css";

    private ExecutionSummary summary;
    private Writer html;
    private Writer styleSheet;

    public HtmlOutputListener() throws IOException {
        this(new FileWriter(DEFAULT_HTML), new FileWriter(DEFAULT_STYLE_SHEET), DEFAULT_PACKAGE_BASE);
    }

    public HtmlOutputListener(String packageBase) throws IOException {
        this(new FileWriter(DEFAULT_HTML), new FileWriter(DEFAULT_STYLE_SHEET), packageBase);
    }

    public HtmlOutputListener(Writer html, Writer styleSheet) {
        this(html, styleSheet, DEFAULT_PACKAGE_BASE);
    }

    public HtmlOutputListener(Writer html, Writer styleSheet, String packageBase) {
        this(html, styleSheet, new ExecutionSummary(packageBase));
    }

    public HtmlOutputListener(Writer html, Writer styleSheet, ExecutionSummary summary) {
        this.summary = summary;
        this.html = html;
        this.styleSheet = styleSheet;
    }

    public void executionSetStarted(ExecutionSet es) {
        summary.executionStarted();
    }

    public void executionSetEnded(ExecutionSet es) {
        try {
            writeStyleSheet(styleSheet, HtmlOutputListener.class.getResourceAsStream("/" + DEFAULT_STYLE_SHEET));
            writeHtml(html, new HtmlOutputBuilder());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write report", e);
        }
    }

    private void writeHtml(Writer writer, HtmlOutputBuilder builder) throws IOException {
        writer.write(builder.buildHtmlHeader());
        writer.write(builder.buildSummaryTable(summary));
        writer.write(builder.buildPackageSummaryTable(summary.getPackageSummaries()));
        writer.write(builder.buildScriptSummaryTables(summary.getPackageSummaries()));
        writer.write(builder.buildLineSummaryTables(summary.getPackageSummaries()));
        writer.write(builder.buildHtmlFooter());
        writer.flush();
        writer.close();
    }

    private void writeStyleSheet(Writer writer, InputStream inputStream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader in = new BufferedReader(streamReader);
        try {
            String line = in.readLine();
            while (line != null) {
                writer.write(line);
                line = in.readLine();
            }
        } finally {
            writer.flush();
            writer.close();
            inputStream.close();
            streamReader.close();
            in.close();
        }
    }

    public void scriptStarted(Script s) {
        summary.scriptStarted(s);
    }

    public void scriptEnded(Script s) {
    }

    public void commandStarted(Command c) {
    }

    public void commandEnded(Command c, Throwable t) {
        summary.commandEnded(c, t);
    }
}

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
package com.exoftware.exactor.parser;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.ExecutionSet;
import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.Script;
import com.exoftware.exactor.command.Unknown;
import com.exoftware.util.FileUtilities;
import com.exoftware.util.QuotedStringTokenizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Parses a file into an Exacptr script.
 *
 * @author Brian Swan
 */
public class ScriptParser {
    private static final String COMMENT_CHARACTER = "#";

    private final ExecutionSet executionSet;

    /**
     * Constructor for creating a ScriptParser.
     *
     * @param executionSet that holds onto context information for parsing the current file e.g. previously parsed
     *                     commands
     */
    public ScriptParser(ExecutionSet executionSet) {
        if (executionSet == null) {
            throw new RuntimeException("ExecutionSet cannot be null");
        }
        this.executionSet = executionSet;
    }

    /**
     * Parses a file into an Exactor script.
     *
     * @param file to be parsed
     * @return exactor script
     */
    public Script parse(File file) {
        if (file == null) {
            throw new RuntimeException("Cannot parse null file");
        }
        Script result = new Script(file);
        String[] lines = FileUtilities.linesFromFile(file);
        StringBuilder buffer = new StringBuilder();
        int multiLine = -1;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.endsWith("\\")) {
            	buffer.append(line.substring(0,  line.length() - 1));
            	if (multiLine == -1) {
            		multiLine = i;
            	}
            } else {
            	if (buffer.length() > 0) {
                	buffer.append(line);
                    parseLine(result, buffer.toString(), multiLine);
                    buffer.setLength(0);
                    multiLine = -1;
            	} else {
                    parseLine(result, line, i);
            	}
            }
        }
        return result;
    }

    private void parseLine(Script result, String line, int i) {
        if (!isCommentLine(line) && !isBlankLine(line)) {
            result.addCommand(createCommandFromLine(line, i + 1));
        }
    }

    private boolean isBlankLine(String line) {
        return line.trim().length() == 0;
    }

    private boolean isCommentLine(String line) {
        return line.startsWith(COMMENT_CHARACTER);
    }

    private Command createCommandFromLine(String line, int lineNumber) {
        ScriptElement scriptElement = new ScriptElement(line);
        Command command = createCommand(scriptElement.commandToken);
        command.setName(scriptElement.commandToken);
        command.setLineNumber(lineNumber);
        addParameters(command, scriptElement.parameterTokens);
        return command;
    }

    private Command createCommand(String commandToken) {
        Command result = executionSet.findCommand(commandToken);
        if (result == null) {
            result = new Unknown(commandToken);
        }
        return result;
    }

    private void addParameters(Command command, String[] parameterTokens) {
        for (int i = 0; i < parameterTokens.length; i++) {
            command.addParameter(new Parameter(parameterTokens[i]));
        }
    }

    private class ScriptElement {
        private static final String WHITESPACE = " \t";

        private String commandToken = "";
        private String[] parameterTokens = new String[0];

        ScriptElement(String line) {
            StringTokenizer tokenizer = new QuotedStringTokenizer(line, WHITESPACE);
            if (tokenizer.hasMoreTokens()) {
                commandToken = tokenizer.nextToken();
            }
            List tokens = new ArrayList();
            while (tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());
            }
            parameterTokens = (String[]) tokens.toArray(new String[0]);
        }
    }
}

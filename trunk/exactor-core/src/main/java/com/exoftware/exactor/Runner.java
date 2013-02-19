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

import com.exoftware.exactor.listener.HtmlOutputListener;
import com.exoftware.exactor.listener.SimpleListener;
import com.exoftware.exactor.parser.ScriptParser;
import com.exoftware.util.FileCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class to run scripts from a file or directory. Script files are plain text files with the
 * extension <code>.act</code>. The format of the script files is as follows;
 * <p/>
 * <pre>
 * <code>
 *    # a comment line
 *    CommandName [param 1] [param 2] ... [param n]
 *    AnotherCommand
 * </code>
 * </pre>
 * <p/>
 * Command names are mapped to instances of the <code>Command</code> class by looking for a
 * class with the same name on the classpath.
 *
 * @author Brian Swan
 */
public class Runner {
    private final File file;
    private final ExecutionSet executionSet;
    private String baseDir;

    /**
     * Main entry point. Expected argument is the name of a script file or a directory
     * containing script files.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        try {
            SimpleListener simpleListener = new SimpleListener();
            Runner runner = new Runner(args[0]);
            runner.addListener(new SimpleListener());
            runner.addListener(new HtmlOutputListener(runner.getBaseDir()));
            runner.run();
            if (simpleListener.errorsOccured()) {
                System.exit(1);
            }
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create a new Runner.
     *
     * @param fileName the name of the file or directory to run.
     * @throws FileNotFoundException if the specified filename is not a file or directory name.
     */
    public Runner(String fileName) throws FileNotFoundException {
        this.file = new File(fileName);
        this.executionSet = new ExecutionSet();
        if (!file.exists()) {
            throw new FileNotFoundException("No such file or directory: " + fileName);
        }
        this.baseDir = setBaseDir(this.file);
    }

    private String setBaseDir(File file) {
        return file.isDirectory() ? file.getAbsolutePath() : file.getParent();
    }

    /**
     * Add a listener for execution events.
     *
     * @param listener the listener to add.
     */
    public void addListener(ExecutionSetListener listener) {
        executionSet.addListener(listener);
    }

    /**
     * Run the script, or all the scripts if the runner was created with a directory name.
     */
    public void run() {
        ScriptParser parser = new ScriptParser(executionSet);
        File[] scriptFiles = FileCollector.filesWithExtension(file,
                ExecutionSet.SCRIPT_EXTENSION);
        for (int i = 0; i < scriptFiles.length; i++) {
            executionSet.addScript(parser.parse(scriptFiles[i]));
        }
        executionSet.execute();
    }

    /**
     * Returns the base directory of the runner. The base directory is the directory passed the
     * constructor, or if a file was supplied, the parent directory of the file.
     *
     * @return the base directory of the runner.
     */
    public String getBaseDir() {
        if (!baseDir.endsWith(File.separator)) {
            return baseDir + File.separator;
        }
        return baseDir;
    }
}

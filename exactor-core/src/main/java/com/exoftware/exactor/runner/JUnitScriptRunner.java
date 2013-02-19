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
package com.exoftware.exactor.runner;

import com.exoftware.exactor.ExecutionSet;
import com.exoftware.util.FileCollector;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;

/**
 * This class is used to allow JUnit to be used to run acceptance tests. It creates a suite
 * of JUnit test cases for each instance of an acceptance test it finds. Each acceptance test
 * is wrapped in a <code>ScriptWrapperTestCase</code> instance. The acceptance tests are found
 * by searching either the current working directory or wherever the test.dir system property is
 * set to point to.
 *
 * @author Sean Hanly
 */

public class JUnitScriptRunner {
    /**
     * Returns a JUnit test suite of acceptance tests. These are found by searching either
     * the current working directory or wherever the test.dir system property is set to,
     * if it is set
     *
     * @return a test suite made up of individual test cases for each acceptance test.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("act-scripts");
        String testDir = System.getProperty("test.dir");
        if (testDir == null) {
            testDir = System.getProperty("user.dir");
        }
        // stop being invoked by ide's that auto discover junit tests
        if (System.getProperty("ide", "false").equals("false")) {
            addTests(new File(testDir), suite);
        }
        return suite;
    }

    private static void addTests(File location, TestSuite testSuite) {
        File[] filesWithExtension = FileCollector.filesWithExtension(location, ExecutionSet.SCRIPT_EXTENSION);
        for (int i = 0; i < filesWithExtension.length; i++) {
            testSuite.addTest(new ScriptWrapperTestCase(filesWithExtension[i]));
        }
    }
}


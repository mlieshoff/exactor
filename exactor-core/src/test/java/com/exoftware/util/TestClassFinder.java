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
package com.exoftware.util;

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.MockCommand;
import junit.framework.TestCase;

import java.util.Set;

/**
 * Test class for <code>ClassFinder</code>.
 *
 * @author Brian Swan
 */
public class TestClassFinder extends TestCase {
    private static final String CLASSPATH = System.getProperty("java.class.path");

    private static final String[] INCLUDES = new String[]{"junit-3.8.1.jar"};

    public void setUp() {
        ClassFinder.clear();
    }

    public void testFindClassFromDirectory() {
        Class c = ClassFinder.findClass("MockCommand", Constants.TEST_DIR + "java");
        assertEquals("com.exoftware.exactor.MockCommand", c.getName());
    }

    public void testFindClassFromClasspathDir() {
        Class c = ClassFinder.findClass("MockCommand", CLASSPATH);
        assertEquals("com.exoftware.exactor.MockCommand", c.getName());
    }

    public void testFindClassFromClasspathJar() {
        Class c = ClassFinder.findClass("TestCase", CLASSPATH);
        assertEquals("junit.framework.TestCase", c.getName());
    }

    public void testFindFullyQualifiedClass() {
        Class c = ClassFinder.findClass("com.exoftware.exactor.MockCommand", Constants.TEST_DIR);
        assertEquals("com.exoftware.exactor.MockCommand", c.getName());
    }

    public void testGetClassnamesFromPath() {
        Set<String> classnames = ClassFinder.getClassnamesFromPath(INCLUDES, Constants.TEST_DIR + "java");
        assertTrue(classnames.contains(MockCommand.class.getName()));
    }

    public void testGetClassnamesFromClasspath() {
        Set<String> classnames = ClassFinder.getClassnamesFromPath(INCLUDES, CLASSPATH);
        assertTrue(classnames.contains(MockCommand.class.getName()));
    }

    public void testGetClassnamesFromJar() {
        Set<String> classnames = ClassFinder.getClassnamesFromPath(INCLUDES, CLASSPATH);
        assertTrue(classnames.contains(TestCase.class.getName()));
    }

}

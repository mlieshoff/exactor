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
package com.exoftware.util;

import com.exoftware.exactor.Constants;
import junit.framework.TestCase;

import java.io.File;

/**
 * Test class for <code>FileCollector</code>.
 *
 * @author Sean Hanly
 */

public class TestFileCollector extends TestCase {
    private static final File BASE_DIRECTORY = new File(Constants.DATA_DIR + "multipledirs");
    private static final String PS = System.getProperty("path.separator");
    private File[] files;

    public void testFilesWithExtension() {
        files = FileCollector.filesWithExtension(BASE_DIRECTORY, ".act");
        assertEquals(2, files.length);
        assertContains("test2.act", files);
        assertContains("test1.act", files);
    }

    public void testFilesWithExtensionFileNotDirectory() {
        files = FileCollector.filesWithExtension(new File(BASE_DIRECTORY + Constants.FS + "dir1", "test1.act"), ".act");
        assertEquals(1, files.length);
        assertContains("test1.act", files);
    }

    public void testFilesWithExtensionFileNotDirectoryIgnored() {
        files = FileCollector.filesWithExtension(new File(BASE_DIRECTORY + Constants.FS + "dir1", "test1.act"), ".act",
                new String[]{"dir1"});
        assertEquals(0, files.length);
    }

    public void testFilesWithName() {
        files = FileCollector.filesWithName(BASE_DIRECTORY, "test1.act");
        assertEquals(1, files.length);
        assertEquals("test1.act", files[0].getName());
    }

    public void testFilesWithNameNoMatches() {
        files = FileCollector.filesWithName(BASE_DIRECTORY, "junk");
        assertEquals(0, files.length);
    }

    public void testFilesWithExtensionIgnoredDirectories() {
        files = FileCollector.filesWithExtension(BASE_DIRECTORY, ".act", new String[]{"dir1"});
        assertEquals(1, files.length);
        assertEquals("test2.act", files[0].getName());
    }

    public void testFilesWithNameIgnoredDirectories() {
        files = FileCollector.filesWithName(BASE_DIRECTORY, "test2.act", new String[]{"dir1"});
        assertEquals(1, files.length);
        assertEquals("test2.act", files[0].getName());
    }

    public void testFileWithNameOnPathSingleEntry() {
        files = FileCollector.filesWithName(Constants.DATA_DIR + "multipledirs", "test1.act");
        assertEquals(1, files.length);
        assertEquals("test1.act", files[0].getName());
    }

    public void testFileWithNameOnPathMultipleEntries() {
        String root = Constants.DATA_DIR + "multipledirs" + Constants.FS;
        String path = root + "dir1" + PS + root + "dir2";
        files = FileCollector.filesWithName(path, "test2.act");
        assertEquals(1, files.length);
        assertEquals("test2.act", files[0].getName());
    }

    public void testFindDirectories() {
        files = FileCollector.directories(new File(Constants.DATA_DIR));
        assertEquals(8, files.length);
        assertContains("multipledirs", files);
        assertContains("dir1", files);
        assertContains("dir2", files);
        assertContains("multiplefiles", files);
    }

    public void testFindDirectoriesWithIgnored() {
        files = FileCollector.directories(new File(Constants.DATA_DIR), new String[]{"dir1", "dir2"});
        assertEquals(6, files.length);
        assertContains("multipledirs", files);
        assertContains("multiplefiles", files);
    }

    private void assertContains(String name, File[] files) {
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(name)) {
                return;
            }
        }
        fail("No file named [" + name + "], found in files.");
    }
}

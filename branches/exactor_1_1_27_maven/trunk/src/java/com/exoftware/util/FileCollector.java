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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Helper class to find files.
 *
 * @author Brian Swan
 */
public class FileCollector
{
    private static final String[] DEFAULT_IGNORED_DIRS = new String[]{"CVS"};

    /**
     * Returns an array of files representing all the files with the supplied
     * extension in the supplied directory or below. By default this method ignores directories
     * named CVS.
     *
     * @param directory the directory to recursively search in.
     * @param extension the extension of the files to return.
     * @return an array of files representing all the files with the supplied
     *         extension in the supplied directory or below.
     */
    public static File[] filesWithExtension( File directory, String extension )
    {
        return collectMatchingFiles( directory, new ExtensionFilter( extension ), DEFAULT_IGNORED_DIRS );
    }

    /**
     * Returns an array of files representing all the files with the supplied
     * extension in the supplied directory or below, ignoring the specified directory names.
     *
     * @param directory   the directory to recursively search in.
     * @param extension   the extension of the files to return.
     * @param ignoredDirs an array of directory names to ignore.
     * @return an array of files representing all the files with the supplied
     *         extension in the supplied directory or below.
     */
    public static File[] filesWithExtension( File directory, String extension, String[] ignoredDirs )
    {
        return collectMatchingFiles( directory, new ExtensionFilter( extension ), ignoredDirs );
    }

    /**
     * Returns an array of files representing all the files with the supplied
     * name in the supplied directory or below. By default this method ignores directories
     * named CVS.
     *
     * @param directory the directory to recursively search in.
     * @param name      the name of the files to return.
     * @return an array of files representing all the files with the supplied
     *         name in the supplied directory or below.
     */
    public static File[] filesWithName( File directory, String name )
    {
        return collectMatchingFiles( directory, new NameFilter( name ), DEFAULT_IGNORED_DIRS );
    }

    /**
     * Returns an array of files representing all the files with the supplied
     * name in the supplied directory or below, ignoring the specified directory names.
     *
     * @param directory   the directory to recursively search in.
     * @param name        the name of the files to return.
     * @param ignoredDirs an array of direcotry names to ignore.
     * @return an array of files representing all the files with the supplied
     *         name in the supplied directory or below.
     */
    public static File[] filesWithName( File directory, String name, String[] ignoredDirs )
    {
        return collectMatchingFiles( directory, new NameFilter( name ), ignoredDirs );
    }

    /**
     * Returns an array of files representing all the files with the supplied name on the searchPath
     * or below. The searchPath is a string of directory or jar names delimited by the system path separator,
     * on Windows the <code>';'</code> character.
     *
     * @param searchPath the path to search for files.
     * @param name       the name of the files to return.
     * @return an array of files representing all the files with the supplied name on the searchPath
     *         or below.
     */
    public static File[] filesWithName( String searchPath, String name )
    {
        List result = new ArrayList();
        FileFilter nameFilter = new NameFilter( name );
        StringTokenizer tokenizer = new StringTokenizer( searchPath, System.getProperty( "path.separator" ) );
        while( tokenizer.hasMoreTokens() )
        {
            File pathElement = new File( tokenizer.nextToken() );
            if( pathElement.isDirectory() )
                addMatchingFiles( pathElement, nameFilter, DEFAULT_IGNORED_DIRS, result );
        }
        return (File[]) result.toArray( new File[0] );
    }

    /**
     * Returns an array of files representing all the directories
     * in the supplied directory or below. By default this method ignores directories
     * named CVS.
     *
     * @param directory the directory to recursively search in.
     * @return an array of files representing all the directories
     *         in the supplied directory or below.
     */
    public static File[] directories( File directory )
    {
        return collectMatchingFiles( directory, new DirectoryFilter( DEFAULT_IGNORED_DIRS ), DEFAULT_IGNORED_DIRS );
    }

    /**
     * Returns an array of files representing all the directories
     * in the supplied directory or below, ignoring the specified directory names.
     *
     * @param directory   the directory to recursively search in.
     * @param ignoredDirs an array of directory names to ignore.
     * @return an array of files representing all directories
     *         in the supplied directory or below.
     */
    public static File[] directories( File directory, String[] ignoredDirs )
    {
        return collectMatchingFiles( directory, new DirectoryFilter( ignoredDirs ), ignoredDirs );
    }

    private static File[] collectMatchingFiles( File directory, FileFilter filter, String[] ignoredDirs )
    {
        List result = new ArrayList();

        if( directory.isFile() )
        {
            if( filter.accept( directory ) && !isIgnored( directory.getParentFile(), ignoredDirs ) )
                result.add( directory );
        }
        else
        {
            addMatchingFiles( directory, filter, ignoredDirs, result );
        }

        return (File[]) result.toArray( new File[0] );
    }

    private static void addMatchingFiles( File directory, FileFilter filter, String[] ignoredDirs, List list )
    {
        File[] files = directory.listFiles();
        for( int i = 0; i < files.length; i++ )
        {
            if( filter.accept( files[i] ) )
                list.add( files[i] );
            if( files[i].isDirectory() && !isIgnored( files[i], ignoredDirs ) )
                addMatchingFiles( files[i], filter, ignoredDirs, list );
        }
    }

    private static boolean isIgnored( File dir, String[] ignoredDirs )
    {
        for( int i = 0; i < ignoredDirs.length; i++ )
            if( dir.getName().equals( ignoredDirs[i] ) )
                return true;

        return false;
    }

    private static class ExtensionFilter implements FileFilter
    {
        private final String extension;

        public ExtensionFilter( String extension )
        {
            this.extension = extension;
        }

        public boolean accept( File pathname )
        {
            return pathname.getName().endsWith( extension );
        }
    }

    private static class NameFilter implements FileFilter
    {
        private final String name;

        public NameFilter( String name )
        {
            this.name = name;
        }

        public boolean accept( File pathname )
        {
            return pathname.getName().equals( name );
        }
    }

    private static class DirectoryFilter implements FileFilter
    {
        private final String[] ignoredDirs;

        public DirectoryFilter( String[] ignoredDirs )
        {
            this.ignoredDirs = ignoredDirs;
        }

        public boolean accept( File pathname )
        {
            return pathname.isDirectory() && !isIgnored( pathname, ignoredDirs );
        }
    }
}

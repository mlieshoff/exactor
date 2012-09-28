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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Utility class to find a class on the class path given just
 * its name.
 *
 * @author Brian Swan
 */
public class ClassFinder
{
    public static final String PACKAGE_SEPARATOR = ".";
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    private static String[] packages;
    private static HashSet<String> cachedPackages = new HashSet<String>();

    /**
     * Attempts to find the named class in the cache if not it looks on the supplied classpath and caches the answer
     * for future lookups.
     *
     * @param name      the name of the class to find, e.g. <code>String</code> for <code>java.lang.String</code>
     * @param classPath the path to search. Multiple entries should be separated by the usual path separator character.
     *
     * @return the class with the supplied name, or <code>null</code> if no matching class can be found.
     */
    public static Class findClass(String name, String classPath) {
        initializePackagesIfNeccessary(classPath);
        Class cachedClass = tryToGetClassFromCachedPackages(name);
        if (cachedClass == null) {
            return getClassFromPackages(name);
        }
        return cachedClass;
    }

    private static void initializePackagesIfNeccessary(String classPath) {
        if (packages == null) {
            packages = getPackagesFromPath(classPath);
        }
    }

    private static Class getClassForName(String className) {
        try {
            return Class.forName( className );
        } catch(ClassNotFoundException ignored) {
            return null;
        }
    }

    public static String[] getPackagesFromPath( String classPath )
    {
        List result = new ArrayList();
        result.add( "" );
        File[] files = filesFromPath( classPath );
        for( int i = 0; i < files.length; i++ )
        {
            if( files[i].isDirectory() )
            {
                List dirNames = new ArrayList();
                addDirectoryNames( FileCollector.directories( files[i] ), dirNames );
                result.addAll( convertToPackageNames( files[i].getAbsolutePath(), dirNames ) );
            }
            else // its a jar file
            {
                List jarDirNames = new ArrayList();
                addDirectoryNamesFromJar( files[i], jarDirNames );
                result.addAll( convertToPackageNames( "", jarDirNames ) );
            }
        }
        return (String[]) result.toArray( new String[0] );
    }

    private static File[] filesFromPath( String path )
    {
        StringTokenizer tokenizer = new StringTokenizer( path, File.pathSeparator );
        File[] result = new File[tokenizer.countTokens()];
        for( int i = 0; tokenizer.hasMoreTokens(); i++ )
            result[i] = new File( tokenizer.nextToken() );

        return result;
    }

    private static void addDirectoryNames( File[] dirs, List dirNames )
    {
        for( int i = 0; i < dirs.length; i++ )
        {
            dirNames.add( dirs[i].getAbsolutePath() );
        }
    }

    private static void addDirectoryNamesFromJar( File jar, List jarDirNames )
    {
        for( Enumeration e = getJarEntries( jar ); e.hasMoreElements(); )
        {
            ZipEntry each = (ZipEntry) e.nextElement();
            if( each.isDirectory() )
                jarDirNames.add( each.getName() );
        }

    }

    private static Enumeration getJarEntries( File jar )
    {
        try
        {
            JarFile jarFile = new JarFile( jar );
            return jarFile.entries();
        }
        catch( IOException e )
        {
            return emptyEnumeration();
        }
    }

    private static Enumeration emptyEnumeration()
    {
        return new Vector().elements();
    }

    private static List convertToPackageNames( String root, List dirNames )
    {
        List result = new ArrayList();
        for( Iterator i = dirNames.iterator(); i.hasNext(); )
        {
            String each = (String) i.next();
            String dirName = each.substring( root.length() == 0 ? 0 : root.length() + 1 );
            result.add( toPackageName( dirName ) );
        }
        return result;
    }

    public static String toPackageName( String dir )
    {
        char separator = '\\';
        if( dir.indexOf( separator ) == -1 )
            separator = '/';

        String result = dir.replace( separator, PACKAGE_SEPARATOR_CHAR );
        if( !result.endsWith( PACKAGE_SEPARATOR ) )
            return result + PACKAGE_SEPARATOR;

        return result;
    }

    private static Class tryToGetClassFromCachedPackages(String name) {
        if (cachedPackages.size() > 0) {
            for (String packageName : cachedPackages) {
                Class result = getClassForName(packageName + name);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private static Class getClassFromPackages(String name) {
        for (int i = 0; i < packages.length; i++) {
            Class result = getClassForName(packages[i] + name);
            if (result != null) {
                cachedPackages.add(packages[i]);
                return result;
            }
        }
        return null;
    }

}

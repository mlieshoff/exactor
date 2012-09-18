package com.exoftware.exactor;

import java.io.File;

/**
 * Class containing constant definitions used
 * by multiple tests.
 *
 * @author Brian Swan
 */
public class Constants
{
    public static final String FS = File.separator;
    public static final String TEST_DIR = System.getProperty( "user.dir" ) + FS + "src" + FS + "test" + FS;
    public static final String DATA_DIR = TEST_DIR + "data" + FS;
    public static final String NEW_LINE = System.getProperty( "line.separator" );

    public static String pathToTestFile(String testfile)
    {
       return Constants.DATA_DIR + testfile;
    }    
}

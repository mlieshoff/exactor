package com.exoftware.util;

import com.exoftware.exactor.Constants;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

public class TestFileUtilities extends TestCase
{
   protected void setUp() throws Exception
   {
      super.setUp();

      new File( Constants.pathToTestFile( "deleteme.txt" ) ).delete();
   }

   public void testGetFileContent( ) throws IOException
   {
      assertEquals( "abc\r\n" +
                    "def\r\n" +
                    "123\r\n",
                    FileUtilities.getFileContent( Constants.pathToTestFile( "file.txt" ) ) );
   }

   public void testWriteFileContent( ) throws Exception
   {
      FileUtilities.writeToFile( Constants.DATA_DIR + "deleteme.txt",
                                 "abc",
                                 false );
      assertEquals( "abc",
                    FileUtilities.getFileContent( Constants.pathToTestFile( "deleteme.txt" ) ) );

      FileUtilities.writeToFile( Constants.pathToTestFile( "deleteme.txt" ),
                                 "def",
                                 false );
      assertEquals( "def",
                    FileUtilities.getFileContent( Constants.pathToTestFile( "deleteme.txt" ) ) );

      FileUtilities.writeToFile( Constants.pathToTestFile( "deleteme.txt" ),
                                 "ghi",
                                 true );
      assertEquals( "defghi",
                    FileUtilities.getFileContent( Constants.pathToTestFile( "deleteme.txt" ) ) );
   }

   public void testMergeDynamicLines( )
   {
      assertEquals( "line1\nline2\nline3\n",
                    FileUtilities.mergeDynamicLines( "line1\ndef\nline3\n", "line1\nline2\nline3\n", new int[]{1} ) );
      assertEquals( "line1\nline2\nline3\n",
                    FileUtilities.mergeDynamicLines( "abc\ndef\nline3\n", "line1\nline2\nline3\n", new int[]{0, 1} ) );
      assertEquals( "line1\nline2\nline3\n",
                    FileUtilities.mergeDynamicLines( "abc\ndef\nghi\n", "line1\nline2\nline3\n", new int[]{0, 1, 2} ) );
      assertEquals( "abc\ndef\nghi\n",
                    FileUtilities.mergeDynamicLines( "abc\ndef\nghi\n", "line1\nline2\nline3\n", new int[]{} ) );
   }

   public void testResolveLocation( )
   {
      assertEquals( "c:/temp/somefile.txt",
                    FileUtilities.resolveLocation( "c:/temp/somefile.txt", "c:/temp" ) );
      assertEquals( "c:/temp" + File.separator + "./somefile.txt",
                    FileUtilities.resolveLocation( "./somefile.txt", "c:/temp" ) );
   }

   public void testContentFromRelativeLocation( ) throws IOException
   {
      assertEquals( "abc\r\n" +
                    "def\r\n" +
                    "123\r\n",
                    FileUtilities.getFileContent( FileUtilities.resolveLocation( Constants.pathToTestFile( "file.txt" ), "does not matter" ) ) );
      assertEquals( "abc\r\n" +
                    "def\r\n" +
                    "123\r\n",
                    FileUtilities.getFileContent( FileUtilities.resolveLocation( "./file.txt", Constants.DATA_DIR ) ) );
      assertEquals( "abc\r\n" +
                    "def\r\n" +
                    "123\r\n",
                    FileUtilities.getFileContent( FileUtilities.resolveLocation( "../file.txt", Constants.DATA_DIR + File.separator + "anotherDir") ) );
   }

}
package com.exoftware.exactor.command.utility;

import java.io.File;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.Script;

public class TestCompareDynamicFiles extends TestCase
{
   public void testCompareFiles_Match() throws Exception
   {
      CompareDynamicFiles compareFiles = new CompareDynamicFiles();
      compareFiles.setScript( new Script( new File(Constants.pathToTestFile( "dummy.act" )) ));

      compareFiles.addParameter( new Parameter( Constants.pathToTestFile( "comparedynamicfiles/expected_file.txt") ) );
      compareFiles.addParameter( new Parameter( Constants.pathToTestFile( "comparedynamicfiles/actual_file_matching.txt") ) );
      compareFiles.addParameter( new Parameter( "1" ) );

      compareFiles.execute();
   }

   public void testCompareFiles_DoNotMatch() throws Exception
   {
      CompareDynamicFiles compareFiles = new CompareDynamicFiles();
      compareFiles.setScript( new Script( new File( Constants.pathToTestFile( "dummy.act" )) ));

      compareFiles.addParameter( new Parameter( Constants.pathToTestFile( "comparedynamicfiles/expected_file.txt") ) );
      compareFiles.addParameter( new Parameter( Constants.pathToTestFile( "comparedynamicfiles/actual_file_not_matching.txt") ) );
      compareFiles.addParameter( new Parameter( "1" ) );

      try
      {
         compareFiles.execute();
      }
      catch ( AssertionFailedError e )
      {
         assertEquals( "Content of expected file: '" + Constants.pathToTestFile( "comparedynamicfiles/expected_file.txt") + "' does not match actual file: '" +
                        Constants.pathToTestFile( "comparedynamicfiles/actual_file_not_matching.txt") + "' expected:<...ghi...> but was:<...xxx...>", e.getMessage() );
      }
   }

   public void testCompareFilesRelative_Match() throws Exception
   {
      CompareDynamicFiles compareFiles = new CompareDynamicFiles();
      compareFiles.setScript( new Script( new File(Constants.pathToTestFile( "comparedynamicfiles/script_for_relative_path_test.act" )) ));

      compareFiles.addParameter( new Parameter( "./expected_file.txt") );
      compareFiles.addParameter( new Parameter( "./actual_file_matching.txt") );
      compareFiles.addParameter( new Parameter( "1" ) );

      compareFiles.execute();
   }

   public void testCompareFilesRelative_DoNotMatch() throws Exception
   {
      CompareDynamicFiles compareFiles = new CompareDynamicFiles();
      compareFiles.setScript( new Script( new File(Constants.pathToTestFile( "comparedynamicfiles/script_for_relative_path_test.act" )) ));

      compareFiles.addParameter( new Parameter( "./expected_file.txt") );
      compareFiles.addParameter( new Parameter( "./actual_file_not_matching.txt") );
      compareFiles.addParameter( new Parameter( "1" ) );

      try
      {
         compareFiles.execute();
      }
      catch ( AssertionFailedError e )
      {
         assertEquals( "Content of expected file: './expected_file.txt' does not match actual file: './actual_file_not_matching.txt' expected:<...ghi...> but was:<...xxx...>", e.getMessage() );
      }
   }

   public void testConvertToLineNumbers()
   {
      int[] ints = CompareDynamicFiles.convertToLineNumbers( "" );
      assertEquals( 0, ints.length);

      ints = CompareDynamicFiles.convertToLineNumbers( "1,2,3" );
      assertEquals( 3, ints.length);
      assertEquals( 1, ints[0]);
      assertEquals( 2, ints[1]);
      assertEquals( 3, ints[2]);
   }
}

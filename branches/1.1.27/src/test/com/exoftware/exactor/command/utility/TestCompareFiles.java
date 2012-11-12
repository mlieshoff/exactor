package com.exoftware.exactor.command.utility;

import junit.framework.TestCase;
import junit.framework.AssertionFailedError;
import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.Script;
import com.exoftware.exactor.Constants;
import com.exoftware.exactor.command.utility.CompareFiles;

import java.io.File;

public class TestCompareFiles extends TestCase
{
   public void testCompareFiles_Match() throws Exception
   {
      CompareFiles compareFiles = new CompareFiles();
      compareFiles.setScript( new Script( new File(Constants.pathToTestFile( "dummy.act" )) ));

      compareFiles.addParameter( new Parameter( Constants.pathToTestFile( "comparefiles/expected_file.txt") ) );
      compareFiles.addParameter( new Parameter( Constants.pathToTestFile( "comparefiles/actual_file_matching.txt") ) );

      compareFiles.execute();
   }

   public void testCompareFiles_DoNotMatch() throws Exception
   {
      CompareFiles compareFiles = new CompareFiles();
      compareFiles.setScript( new Script( new File(Constants.pathToTestFile( "dummy.act" )) ));

      compareFiles.addParameter( new Parameter( Constants.pathToTestFile( "comparefiles/expected_file.txt") ) );
      compareFiles.addParameter( new Parameter( Constants.pathToTestFile( "comparefiles/actual_file_not_matching.txt") ) );

      try
      {
         compareFiles.execute();
      }
      catch ( AssertionFailedError e )
      {
         assertEquals( "Content of expected file: '" + Constants.pathToTestFile( "comparefiles/expected_file.txt") + "' does not match actual file: '" +
                        Constants.pathToTestFile( "comparefiles/actual_file_not_matching.txt") + "' expected:<...def...> but was:<...xxx...>", e.getMessage() );
      }
   }

   public void testCompareFilesRelative_Match() throws Exception
   {
      CompareFiles compareFiles = new CompareFiles();
      compareFiles.setScript( new Script( new File(Constants.pathToTestFile( "comparefiles/script_for_relative_path_test.act" )) ));

      compareFiles.addParameter( new Parameter( "./expected_file.txt") );
      compareFiles.addParameter( new Parameter( "./actual_file_matching.txt") );

      compareFiles.execute();
   }

   public void testCompareFilesRelative_DoNotMatch() throws Exception
   {
      CompareFiles compareFiles = new CompareFiles();
      compareFiles.setScript( new Script( new File(Constants.pathToTestFile( "comparefiles/script_for_relative_path_test.act" )) ));

      compareFiles.addParameter( new Parameter( "./expected_file.txt") );
      compareFiles.addParameter( new Parameter( "./actual_file_not_matching.txt") );

      try
      {
         compareFiles.execute();
      }
      catch ( AssertionFailedError e )
      {
         assertEquals( "Content of expected file: './expected_file.txt' does not match actual file: './actual_file_not_matching.txt' expected:<...def...> but was:<...xxx...>", e.getMessage() );
      }
   }
}

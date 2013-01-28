package com.exoftware.exactor.command.utility;

import com.exoftware.exactor.Command;
import com.exoftware.util.FileUtilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks for a particular pattern in the content of a file. Third boolean parameter says whether the pattern should be
 * present or not. Can use regex for pattern matching. For example:
 *
 * <code>
 * # check for aaa present in file file.txt
 * CheckForPatternInFile afile.txt aaa true
 *
 * # check for aaa not present in file file.txt
 * CheckForPatternInFile afile.txt aaa false
 *
 * </code>
 *
 */

public class CheckForPatternInFile extends Command
{
   public void execute() throws Exception
   {
      String   fileName          = getParameter( 0 ).stringValue();
      String   regex             = getParameter( 1 ).stringValue();
      boolean  expectedToFind    = getParameter( 2 ).booleanValue();
      String   resolvedFileName  = resolveFileRelativeToScriptFile( fileName );
      String   fileContent       = FileUtilities.getFileContent( resolvedFileName );
      Pattern  pattern           = Pattern.compile(regex);
      Matcher  matcher           = pattern.matcher(fileContent);

      if(expectedToFind)
      {
         assertTrue( "Failed to find pattern: '" + regex + "' in file: " + fileName,
                      matcher.find());
      }
      else
      {
         assertFalse( "Should not have found pattern: '" + regex + "' in file: " + fileName,
                      matcher.find());
      }
   }
}



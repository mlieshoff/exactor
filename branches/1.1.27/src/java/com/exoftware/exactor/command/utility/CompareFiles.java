package com.exoftware.exactor.command.utility;

import com.exoftware.exactor.Command;
import com.exoftware.util.FileUtilities;

/**
 * Compare the contents of two files.
 *
 */
public class CompareFiles extends Command
{
   public void execute() throws Exception
   {
      String expectedFileName         = getParameter( 0 ).stringValue();
      String actualFileName           = getParameter( 1 ).stringValue();
      String expectedFileNameResolved = resolveFileRelativeToScriptFile( expectedFileName );
      String actualFileNameResolved   = resolveFileRelativeToScriptFile( actualFileName );
      String expectedContent          = FileUtilities.getFileContent( expectedFileNameResolved );
      String actualContent            = FileUtilities.getFileContent( actualFileNameResolved );

      assertEquals( "Content of expected file: '" + expectedFileName + "' does not match actual file: '" + actualFileName + "'",
                    expectedContent,
                    actualContent);
   }
}

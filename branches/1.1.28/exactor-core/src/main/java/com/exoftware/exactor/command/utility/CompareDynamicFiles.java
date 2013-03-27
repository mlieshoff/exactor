package com.exoftware.exactor.command.utility;

import com.exoftware.exactor.Command;
import com.exoftware.util.FileUtilities;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Compare two files. However before comparing merge any dynamic information from the actual file e.g. timestamps. For
 * example:
 * <p/>
 * <code>
 * # compare expected.txt to actual.txt merging in lines 2 and 4
 * CompareDynamicFiles expected.txt actual.txt 2,4
 * </code>
 */

public class CompareDynamicFiles extends Command {
    public void execute() throws Exception {
        String expectedFileName = getParameter(0).stringValue();
        String actualFileName = getParameter(1).stringValue();
        String expectedFileNameResolved = resolveFileRelativeToScriptFile(expectedFileName);
        String actualFileNameResolved = resolveFileRelativeToScriptFile(actualFileName);
        String expectedContent = FileUtilities.getFileContent(expectedFileNameResolved);
        String actualContent = FileUtilities.getFileContent(actualFileNameResolved);
        String mergeLines = getParameter(2).stringValue();
        int[] mergeLinesNumbers = convertToLineNumbers(mergeLines);
        expectedContent = FileUtilities.mergeDynamicLines(expectedContent, actualContent, mergeLinesNumbers);
        assertEquals("Content of expected file: '" + expectedFileName + "' does not match actual file: '"
                + actualFileName + "'", expectedContent, actualContent);
    }


    static int[] convertToLineNumbers(String mergeLines) {
        StringTokenizer toker = new StringTokenizer(mergeLines, ",");
        ArrayList lineNumbers = new ArrayList();
        while (toker.hasMoreTokens()) {
            lineNumbers.add(new Integer(Integer.parseInt(toker.nextToken())));
        }
        return convertToArray(lineNumbers);
    }

    private static int[] convertToArray(ArrayList lineNumbers) {
        int[] lineNumbersArray = new int[lineNumbers.size()];
        for (int i = 0; i < lineNumbersArray.length; i++) {
            lineNumbersArray[i] = ((Integer) lineNumbers.get(i)).intValue();
        }
        return lineNumbersArray;
    }
}

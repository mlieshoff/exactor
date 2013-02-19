package com.exoftware.exactor.command.utility;

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.Script;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import java.io.File;

public class TestCheckForPatternInFile extends TestCase {
    public static final String PATTERN_FILE = "./patterns.txt";
    public static final String FULL_PATH_TO_PATTERN_FILE = Constants.pathToTestFile(
            "checkforpatterninfile/patterns.txt");

    public void testFindPatternInFile_True() throws Exception {
        CheckForPatternInFile checkForPatternInFile = new CheckForPatternInFile();
        checkForPatternInFile.setScript(new Script(new File(Constants.pathToTestFile("dummy.act"))));
        checkForPatternInFile.addParameter(new Parameter(FULL_PATH_TO_PATTERN_FILE));
        checkForPatternInFile.addParameter(new Parameter("Error"));
        checkForPatternInFile.addParameter(new Parameter("true"));
        checkForPatternInFile.execute();
    }

    public void testFindPatternInFile_False() throws Exception {
        CheckForPatternInFile checkForPatternInFile = new CheckForPatternInFile();
        checkForPatternInFile.setScript(new Script(new File(Constants.pathToTestFile("dummy.act"))));
        checkForPatternInFile.addParameter(new Parameter(FULL_PATH_TO_PATTERN_FILE));
        checkForPatternInFile.addParameter(new Parameter("Error"));
        checkForPatternInFile.addParameter(new Parameter("false"));
        try {
            checkForPatternInFile.execute();
        } catch (AssertionFailedError e) {
            assertEquals("Should not have found pattern: 'Error' in file: " + FULL_PATH_TO_PATTERN_FILE, e.getMessage())
                    ;
        }
    }

    public void testFindPatternInFileRelativeToScript_True() throws Exception {
        CheckForPatternInFile checkForPatternInFile = new CheckForPatternInFile();
        checkForPatternInFile.setScript(new Script(new File(Constants.pathToTestFile(
                "checkforpatterninfile/script_for_relative_path_test.act"))));
        checkForPatternInFile.addParameter(new Parameter(PATTERN_FILE));
        checkForPatternInFile.addParameter(new Parameter("Error"));
        checkForPatternInFile.addParameter(new Parameter("true"));
        checkForPatternInFile.execute();
    }

    public void testFindPatternInFileRelativeToScript_False() throws Exception {
        CheckForPatternInFile checkForPatternInFile = new CheckForPatternInFile();
        checkForPatternInFile.setScript(new Script(new File(Constants.pathToTestFile(
                "checkforpatterninfile/script_for_relative_path_test.act"))));
        checkForPatternInFile.addParameter(new Parameter(PATTERN_FILE));
        checkForPatternInFile.addParameter(new Parameter("Error"));
        checkForPatternInFile.addParameter(new Parameter("false"));
        try {
            checkForPatternInFile.execute();
        } catch (AssertionFailedError e) {
            assertEquals("Should not have found pattern: 'Error' in file: " + PATTERN_FILE, e.getMessage());
        }
    }
}

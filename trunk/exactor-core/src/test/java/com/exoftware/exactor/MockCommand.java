package com.exoftware.exactor;

import junit.framework.AssertionFailedError;

/**
 * Stub implementation of <code>Command</code>
 * for testing.
 *
 * @author Brian Swan
 */
public class MockCommand extends Command {
    public static int staticExecuteCalledCount = 0;
    public int executeCalled;
    public boolean throwAssertionFailedError;
    public boolean throwException;

    public void execute() throws Exception {
        executeCalled++;
        staticExecuteCalledCount++;
        if (throwAssertionFailedError) {
            throw new AssertionFailedError("Testing assertion failure");
        }
        if (throwException) {
            throw new Exception("Testing exception");
        }
    }
}

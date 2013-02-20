package com.exoftware.exactor.command.swing;

import junit.framework.AssertionFailedError;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Abstract command for common functionality shared by all Swing commands
 *
 * @author Sean Hanly
 */
public abstract class AbstractSwingAssertCommand extends AbstractSwingCommand {
    private Exception exception = null;

    public synchronized void execute() throws Exception {
        exception = null;
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                try {
                    doExecute();
                } catch (Exception e) {
                    exception = e;
                }
            }
        });

        if (exception != null) {
            handleException();
        }
    }

    private void handleException() throws Exception {
        if (exception instanceof InvocationTargetException) {
            handleInvocationException();
        } else {
            throw exception;
        }
    }

    private void handleInvocationException() throws Exception {
        Throwable cause = ((InvocationTargetException) exception).getCause();
        if (cause instanceof AssertionFailedError) {
            throw (AssertionFailedError) cause;
        } else {
            throw exception;
        }
    }

    protected abstract void doExecute() throws Exception;
}

package com.exoftware.exactor.extensions;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.ExecutionSet;
import com.exoftware.exactor.ExecutionSetListener;
import com.exoftware.exactor.Script;

/**
 * An extension which makes commands pause for a defined period. Can be added to a <code>Runner</code>
 * instance e.g. <code>runner.addListener( new PauserExtension() );</code>
 * <p/>
 * Can be controlled by setting context propertie e.g.
 * <pre>
 * # Turn on pauser
 * SetProperty  pause_on    true
 *
 * # Set pause amount to 5 seconds, defaults to 1 second
 * SetProperty  pause_value    5000
 *
 * </pre>
 */

public class PauserExtension implements ExecutionSetListener {
    private Pauser pauser = new Pauser() {
        public void pause(long milliseconds) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    };

    static final String PAUSE_ON = "pause_on";
    static final String PAUSE_VALUE = "pause_value";
    static final String DEFAULT_PAUSE_AMOUNT = "1000";

    /**
     * seter to allow fake pauser to be set for testing purposes.
     *
     * @param pauser fake pauser
     */
    void setPauser(Pauser pauser) {
        this.pauser = pauser;
    }

    public void executionSetStarted(ExecutionSet es) {
    }

    public void executionSetEnded(ExecutionSet es) {
    }

    public void scriptStarted(Script s) {
    }

    public void scriptEnded(Script s) {
    }

    public void commandStarted(Command c) {
    }

    /**
     * Pause at the end of each command depending on properties set.
     *
     * @param c command just executed
     * @param t throwable which will be set if command errored
     */
    public void commandEnded(Command c, Throwable t) {
        if (shouldPause(c)) {
            pauser.pause(Long.parseLong(getPauseValue(c)));
        }
    }

    private boolean shouldPause(Command c) {
        String shouldPause = (String) c.getScript().getContext().get(PAUSE_ON);
        if (shouldPause == null) {
            return true;
        }
        return shouldPause.equals("true");
    }

    private String getPauseValue(Command c) {
        String milliseconds = (String) c.getScript().getContext().get(PAUSE_VALUE);
        if (milliseconds == null) {
            milliseconds = DEFAULT_PAUSE_AMOUNT;
        }
        return milliseconds;
    }
}

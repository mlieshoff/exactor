package com.exoftware.exactor.listener;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Script;
import junit.framework.AssertionFailedError;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageSummary {
    private List<ScriptSummary> scriptSummaries = new ArrayList<ScriptSummary>();
    private final String packageName;
    private ScriptSummary currentSummary;
    private int failureCount;
    private int errorCount;

    public PackageSummary(String packageName) {
        this.packageName = packageName;
    }

    public int getScriptsRunCount() {
        return scriptSummaries.size();
    }

    public int getFailureCount() {
        return failureCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public boolean hasPassed() {
        return getFailureCount() == 0 && getErrorCount() == 0;
    }

    public ScriptSummary[] getScriptSummaries() {
        return scriptSummaries.toArray(new ScriptSummary[scriptSummaries.size()]);
    }

    public String getPackageName() {
        return packageName;
    }

    public void scriptStarted(Script script) {
        currentSummary = new ScriptSummary(new File(script.getAbsolutePath()));
        scriptSummaries.add(currentSummary);
    }

    public void commandEnded(Command command, Throwable throwable) {
        if (throwable instanceof AssertionFailedError) {
            failureCount++;
        } else if (throwable != null) {
            errorCount++;
        }
        currentSummary.commandEnded(command, throwable);
    }

    public long getExecutionTime() {
        long executionTime = 0;
        for (ScriptSummary scriptSummary : scriptSummaries) {
            executionTime += scriptSummary.getExecutionTime();
        }
        return executionTime;
    }
}

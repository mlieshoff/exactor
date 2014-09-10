package com.exoftware.exactor.jenkins;

import hudson.model.Action;
import org.apache.commons.io.FileUtils;
import org.kohsuke.stapler.export.Exported;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Andoni del Olmo
 */
public class AcceptanceReportAction implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptanceReportAction.class.getName());

    private static final String LOG_FILE_NAME = "at_out-{0}.log";

    @Exported(visibility = 0)
    private URLChecker urlChecker = new JavaNetURLChecker();

    private String reportBasePath;
    private String reportFileName;
    private int buildVersion;
    private String logPath;
    private String publishDirectory;
    private String encodedAuthBytes;

    public AcceptanceReportAction(String reportBaseUrl, String reportFileName, int buildNumber, String logPath, String publishDirectory, String encodedAuthBytes) {
        this.reportBasePath = reportBaseUrl;
        this.reportFileName = reportFileName;
        this.buildVersion = buildNumber;
        this.logPath = logPath;
        this.publishDirectory = publishDirectory;
        this.encodedAuthBytes = encodedAuthBytes;
        this.copyLogFile();
    }

    @Exported(visibility = 0)
    public void setUrlChecker(URLChecker urlChecker) {
        this.urlChecker = urlChecker;
    }

    /*
    * (non-Javadoc)
    *
    * @see hudson.model.Action#getIconFileName()
    */
    public String getIconFileName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hudson.model.Action#getDisplayName()
     */
    public String getDisplayName() {
        return "Acceptance Test Report";
    }

    /*
     * (non-Javadoc)
     * 
     * @see hudson.model.Action#getUrlName()
     */
    public String getUrlName() {
        return "act";
    }

    /**
     * Checks whether the Acceptance Test Report file exists and it's reachable via HTTP.
     *
     * @return true if the ACT Report is reachable, false otherwise.
     */
    @Exported(name = "doesReportExists")
    public boolean getDoesReportExists() {
        String reportUrl = getReportURL();
        this.urlChecker.setServerAuth(encodedAuthBytes);
        boolean isReportReachable = this.urlChecker.isUrlReachable(reportUrl);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Report at [" + reportUrl
                    + "] is reachable [" + isReportReachable + "]");
        return isReportReachable;
    }

    /**
     * Checks whether the Exit Report log file exists and it's reachable via HTTP.
     *
     * @return true if the Exit Report log is reachable, false otherwise.
     */
    @Exported(name = "doesExitReportExists")
    public boolean getDoesExitReportExists() {
        String exitReportUrl = getLogURL();
        this.urlChecker.setServerAuth(encodedAuthBytes);
        boolean isExitReportReachable = this.urlChecker.isUrlReachable(exitReportUrl);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Exit Report at [" + exitReportUrl
                    + "] is reachable [" + isExitReportReachable + "]");
        return isExitReportReachable;
    }

    /**
     * Gets the URL of the Acceptance Test Report.
     *
     * @return URL of the ACT Report.
     */
    @Exported(name = "reportURL")
    public String getReportURL() {
        return this.reportBasePath + getReportFileName();
    }

    /**
     * Gets the URL of the log file created while the generation of the Acceptance Test Report.
     *
     * @return URL of the log file.
     */
    @Exported(name = "logURL")
    public String getLogURL() {
        return this.reportBasePath + getLogFileName();
    }

    private String getReportFileName() {
        Object[] data = new Object[1];
        data[0] = getBuildNumber();
        return MessageFormat.format(this.reportFileName, data);
    }

    private String getBuildNumber() {
        return Integer.toString(buildVersion);
    }

    private String getLogFileName() {
        Object[] data = new Object[1];
        data[0] = this.buildVersion;
        return MessageFormat.format(LOG_FILE_NAME, data);
    }

    private String getApachePublishLogFile() {
        return this.publishDirectory + getLogFileName();
    }

    private void copyLogFile() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Copying [" + this.logPath + "] to [" + getApachePublishLogFile() + "]...");
        try {
            FileUtils.copyFile(new File(this.logPath), new File(getApachePublishLogFile()));

            if (LOGGER.isInfoEnabled())
                LOGGER.info("Copy of [" + this.logPath + "] to [" + getApachePublishLogFile() + "] was successful.");

        } catch (IOException e) {
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("Cannot add exit log file [" + this.logPath
                        + "] to publishing directory [" + getApachePublishLogFile()
                        + "]. Reason: " + e.getMessage() + ".");
        }
    }
}
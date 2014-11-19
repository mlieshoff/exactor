package com.exoftware.exactor.jenkins;

import hudson.model.Action;
import org.kohsuke.stapler.export.Exported;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andoni del Olmo
 */
public class AcceptanceReportAction implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptanceReportAction.class.getName());

    @Exported(visibility = 0)
    private URLChecker urlChecker = new JavaNetURLChecker();

    private String reportBaseUrl;
    private String encodedAuthBytes;
    private String reportFileName;
    private String logFileName;

    public AcceptanceReportAction(String reportBaseUrl, String encodedAuthBytes, String reportFileName, String logFileName) {
        this.reportBaseUrl = reportBaseUrl;
        this.encodedAuthBytes = encodedAuthBytes;
        this.reportFileName = reportFileName;
        this.logFileName = logFileName;
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
        return this.reportBaseUrl + this.reportFileName;
    }

    /**
     * Gets the URL of the log file created while the generation of the Acceptance Test Report.
     *
     * @return URL of the log file.
     */
    @Exported(name = "logURL")
    public String getLogURL() {
        return this.reportBaseUrl + this.logFileName;
    }

}
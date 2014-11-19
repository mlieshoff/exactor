package com.exoftware.exactor.jenkins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Job;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import jenkins.model.Jenkins;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andoni del Olmo
 */
public class AcceptanceReportPublisher extends Publisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptanceReportPublisher.class.getName());

    public String reportBaseUrl;
    public CredentialsBlock credentials;
    public String publishDirectory;
    public String reportLocation;
    public String logLocation;
    public String project;

    // only for testing, do not use!!
    public AcceptanceReportPublisher() {
    }

    @DataBoundConstructor
    public AcceptanceReportPublisher(String reportBaseUrl, CredentialsBlock credentials, String publishDirectory, String reportLocation, String logLocation, String project) {

        this.reportBaseUrl = reportBaseUrl;
        this.credentials = credentials;
        this.publishDirectory = publishDirectory;
        this.reportLocation = reportLocation;
        this.logLocation = logLocation;
        this.project = project;
    }

    private String getReportBaseUrl() {
        String baseUrl = reportBaseUrl;
        if (LOGGER.isInfoEnabled())
            LOGGER.info("baseUrl[" + baseUrl + "] before");

        if (!reportBaseUrl.endsWith("/"))
            baseUrl = baseUrl.concat("/");

        if (LOGGER.isInfoEnabled())
            LOGGER.info("baseUrl[" + baseUrl + "] after");
        return baseUrl;
    }

    private String getReportLocation() {
        return reportLocation;
    }

    private String getLogLocation() {
        return logLocation;
    }

    private String getPublishDirectory() {
        String publishDirectory = this.publishDirectory;
        if (LOGGER.isInfoEnabled())
            LOGGER.info("publishDirectory[" + publishDirectory + "] before");

        if (!this.publishDirectory.endsWith(File.separator))
            publishDirectory = publishDirectory.concat(File.separator);

        if (LOGGER.isInfoEnabled())
            LOGGER.info("publishDirectory[" + publishDirectory + "] after");
        return publishDirectory;
    }

    private CredentialsBlock getCredentials() {
        return credentials;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {

        String reportBaseUrlParam = getReportBaseUrl();
        String authCredentials = getAuthCredentials(credentials);
        String reportLocationParam = getReportLocation();
        String logLocationParam = getLogLocation();
        int buildNumberParam = getBuildNumber(build);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("perform > reportBaseUrl[" + reportBaseUrlParam
                    + "] auth[" + authCredentials
                    + "] reportLocationParam[" + reportLocationParam
                    + "] logLocation[" + logLocationParam
                    + "] buildNumber[" + buildNumberParam
                    + "].");

        boolean reportCreated = true;

        final String reportFileName = getReportFileName(buildNumberParam);
        final String logFileName = getLogFileName(buildNumberParam);

        copyReportFile(reportLocationParam, reportFileName);
        copyLogFile(logLocationParam, logFileName);

        build.addAction(new AcceptanceReportAction(reportBaseUrlParam, authCredentials, reportFileName, logFileName));
        return reportCreated;
    }

    private int getBuildNumber(AbstractBuild<?, ?> build) {
        int buildNumber;
        if (StringUtils.equals(build.getProject().getName(), this.project)) {
            buildNumber = build.getNumber();
        } else {
            buildNumber = getBuildNumberForProject(this.project);
        }
        LOGGER.info("buildNumber [" + buildNumber + "].");
        return buildNumber;
    }

    private int getBuildNumberForProject(String projectName) {
        final List<Job> jobList = new ArrayList<Job>(Jenkins.getInstance().getItem(projectName).getAllJobs());
        return jobList.get(jobList.size() - 1).getLastCompletedBuild().getNumber();
    }

    private String getAuthCredentials(CredentialsBlock credentials) {
        if (credentials != null) {
            return ServerAuth.getEncodedAuth(credentials.getServerAuthUsername(), credentials.getServerAuthPassword());
        } else {
            return "";
        }
    }

    private void copyReportFile(String reportFileName, String fileName) {
        copyFileToPublishDirectory(reportFileName, fileName);
    }

    private void copyLogFile(String logFileName, String fileName) {
        copyFileToPublishDirectory(logFileName, fileName);
    }

    private void copyFileToPublishDirectory(String srcFile, String fileName) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Copying [" + this.getPublishDirectory() + "] to [" + srcFile + "]...");
        final File destFile = new File(getPublishDirectory(), fileName);
        try {
            FileUtils.copyFile(new File(srcFile), destFile, false);

            if (LOGGER.isInfoEnabled())
                LOGGER.info("Copy of [" + srcFile + "] to [" + destFile.getAbsolutePath() + "] was successful.");

        } catch (IOException e) {
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("Cannot copy file [" + this.logLocation
                        + "] to publishing directory [" + destFile.getAbsolutePath()
                        + "]. Reason: " + e.getMessage() + ".");
        }
    }

    private String getReportFileName(int buildVersion) {
        return getFileNameWithVersion(reportLocation, buildVersion);
    }

    private String getLogFileName(int buildVersion) {
        return getFileNameWithVersion(logLocation, buildVersion);
    }

    private String getFileNameWithVersion(String location, int buildVersion) {
        String name = FilenameUtils.getBaseName(location);
        String extension = FilenameUtils.getExtension(location);
        return name.concat(String.format("-%d", buildVersion)).concat(".").concat(extension);
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Override
    public BuildStepDescriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }

    @Extension
    public static final AcceptanceReportPublisherDescriptor DESCRIPTOR = new AcceptanceReportPublisherDescriptor();

    public static class CredentialsBlock {
        private String serverAuthUsername;
        private String serverAuthPassword;

        @DataBoundConstructor
        public CredentialsBlock(String serverAuthUsername, String serverAuthPassword) {
            this.serverAuthUsername = serverAuthUsername;
            this.serverAuthPassword = serverAuthPassword;
        }

        public String getServerAuthUsername() {
            return serverAuthUsername;
        }

        public String getServerAuthPassword() {
            return serverAuthPassword;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CredentialsBlock that = (CredentialsBlock) o;

            if (serverAuthPassword != null ? !serverAuthPassword.equals(that.serverAuthPassword) : that.serverAuthPassword != null)
                return false;
            if (serverAuthUsername != null ? !serverAuthUsername.equals(that.serverAuthUsername) : that.serverAuthUsername != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = serverAuthUsername != null ? serverAuthUsername.hashCode() : 0;
            result = 31 * result + (serverAuthPassword != null ? serverAuthPassword.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("CredentialsBlock{");
            sb.append("serverAuthUsername [").append(serverAuthUsername).append(']');
            sb.append(", serverAuthPassword [").append(serverAuthPassword).append(']');
            sb.append('}');
            return sb.toString();
        }
    }
}
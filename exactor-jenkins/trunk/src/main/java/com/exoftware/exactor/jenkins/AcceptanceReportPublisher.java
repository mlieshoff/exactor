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
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Andoni del Olmo
 */
public class AcceptanceReportPublisher extends Publisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptanceReportPublisher.class.getName());

    public String reportBaseUrl;
    public String reportFileName;
    public String logPath;
    public String publishDirectory;
    public String project;
    public CredentialsBlock credentials;

    // only for testing, do not use!!
    public AcceptanceReportPublisher() {
    }

    @DataBoundConstructor
    public AcceptanceReportPublisher(String reportBaseUrl, String reportFileName,
                                     String logPath, String publishDirectory, String project,
                                     CredentialsBlock credentials) {
        this.reportBaseUrl = reportBaseUrl;
        this.reportFileName = reportFileName;
        this.logPath = logPath;
        this.publishDirectory = publishDirectory;
        this.project = project;
        this.credentials = credentials;
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

    private String getReportFileName() {
        return reportFileName;
    }

    private String getLogPath() {
        return logPath;
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
        String reportFileNameParam = getReportFileName();
        int buildNumberParam = getBuildNumber(build);
        String logPathParam = getLogPath();
        String publishDirectoryParam = getPublishDirectory();
        String authCredentials = getAuthCredentials(credentials);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("perform > reportBaseUrl[" + reportBaseUrlParam
                    + "] reportFileName[" + reportFileNameParam
                    + "] buildNumber[" + buildNumberParam
                    + "] logPath[" + logPathParam
                    + "] publishDirectory[" + publishDirectoryParam
                    + "] auth[" + authCredentials + "].");

        boolean reportCreated = true;
        build.addAction(new AcceptanceReportAction(reportBaseUrlParam, reportFileNameParam,
                buildNumberParam, logPathParam, publishDirectoryParam, authCredentials));
        return reportCreated;
    }

    private int getBuildNumber(AbstractBuild<?, ?> build) {
        int buildNumber;
        if (StringUtils.equals(build.getProject().getName(), this.project)) {
            buildNumber = build.getNumber();
        } else {
            buildNumber = getCurrentBuildNumberForProject(this.project);
        }
        LOGGER.info("buildNumber [" + buildNumber + "].");
        return buildNumber;
    }

    private int getCurrentBuildNumberForProject(String projectName) {
        try {
            int buildNumber;
            ArrayList<Job> jobList = new ArrayList<Job>(Jenkins.getInstance().getItem(projectName).getAllJobs());
            LOGGER.debug("project [" + projectName + "] has #" + jobList.size() + " job builds.");
            buildNumber = jobList.get(jobList.size() - 1).getNextBuildNumber() - 1;
            return buildNumber;
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve build number for project \"" + projectName + "\".", e);
            return 0;
        }
    }

    private String getAuthCredentials(CredentialsBlock credentials) {
        if (credentials != null) {
            return ServerAuth.getEncodedAuth(credentials.getServerAuthUsername(), credentials.getServerAuthPassword());
        } else {
            return "";
        }
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
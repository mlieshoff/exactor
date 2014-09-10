package com.exoftware.exactor.jenkins;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Andoni del Olmo
 */
public class AcceptanceReportPublisherDescriptor extends BuildStepDescriptor<Publisher> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptanceReportPublisherDescriptor.class.getName());

    private URLChecker urlChecker = new JavaNetURLChecker();

    public AcceptanceReportPublisherDescriptor() {
        super(AcceptanceReportPublisher.class);
        load();
    }

    @Override
    public String getDisplayName() {
        return "Acceptance Test Reporting";
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
    }

    @Override
    public String getHelpFile() {
        return "/plugin/acceptance-report-plugin/help/help-globalConfig.html";
    }

    public FormValidation doCheckReportBaseUrl(@QueryParameter final String value)
            throws IOException, ServletException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("doCheckReportBaseUrl [" + value + "]");

        if (StringUtils.isEmpty(value))
            return FormValidation.error("Please set a URL base path.");
        if (!value.contains("http"))
            return FormValidation.error("Please set a valid URL base path.");
        return FormValidation.ok();
    }

    public FormValidation doTestConnection(@QueryParameter("reportBaseUrl") final String reportBaseUrl,
                                           @QueryParameter("serverAuthUsername") final String serverAuthUsername,
                                           @QueryParameter("serverAuthPassword") final String serverAuthPassword) throws IOException, ServletException {
        this.urlChecker.setServerAuth(ServerAuth.getEncodedAuth(serverAuthUsername, serverAuthPassword));
        if (this.urlChecker.isUrlReachable(reportBaseUrl)) {
            return FormValidation.ok("Success");
        }
        return FormValidation.error("Cannot establish connection");
    }

    public FormValidation doCheckPublishDirectory(@QueryParameter final String value)
            throws IOException, ServletException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("doCheckPublishDirectory [" + value + "]");

        if (StringUtils.isEmpty(value))
            return FormValidation.error("Please set a valid directory.");
        File directory = new File(value);
        if (!directory.exists() || !directory.isDirectory())
            return FormValidation.error("Please set an existing directory.");
        return FormValidation.ok();
    }

    public FormValidation doCheckReportFileName(@QueryParameter final String value)
            throws IOException, ServletException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("doCheckReportFileName [" + value + "]");

        if (StringUtils.isEmpty(value))
            return FormValidation.error("Please set a valid file mask.");
        if (!value.contains("{0}"))
            return FormValidation.error("Please set a valid file mask. Missing \"{0}\" in the mask.");
        return FormValidation.ok();
    }

    public FormValidation doCheckLogPath(@QueryParameter final String value)
            throws IOException, ServletException {
        if (LOGGER.isDebugEnabled())
            LOGGER.info("doCheckLogPath [" + value + "]");

        if (StringUtils.isEmpty(value))
            return FormValidation.error("Please set the path where the log file is located.");
        return FormValidation.ok();
    }

    public FormValidation doCheckProject(@QueryParameter final String value) {
        if (LOGGER.isDebugEnabled())
            LOGGER.info("doCheckLogPath [" + value + "]");
        return FormValidation.ok();
    }

    public ListBoxModel doFillProjectItems() {
        ListBoxModel items = new ListBoxModel();
        List<String> jobNames = getProjects();
        LOGGER.debug("Job list [" + jobNames + "].");
        ListBoxModel.Option option;
        for (String job : jobNames) {
            option = new ListBoxModel.Option(job, job);
            items.add(option);
        }
        return items;
    }

    private List<String> getProjects() {
        List<String> projectList = new ArrayList<String>();
        Collection<String> jobNames = Jenkins.getInstance().getJobNames();
        for (String jobName : jobNames) {
            if (StringUtils.containsNone(jobName, "/")) {
                projectList.add(jobName);
            }
        }
        return projectList;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        save();
        return super.configure(req, formData);
    }
}
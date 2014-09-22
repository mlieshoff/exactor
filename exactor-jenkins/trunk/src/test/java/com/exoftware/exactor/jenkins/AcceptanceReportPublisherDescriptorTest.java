package com.exoftware.exactor.jenkins;


import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.util.FormValidation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Andoni del Olmo
 */
public class AcceptanceReportPublisherDescriptorTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    private AcceptanceReportPublisher publisher;
    private FreeStyleProject project;

    @Before
    public void setUp() throws Exception {
        this.publisher = new AcceptanceReportPublisher();
        this.project = j.createFreeStyleProject();
        this.project.getPublishersList().add(this.publisher);
    }

    @Test
    public void testGetDisplayName() throws Exception {
        assertEquals("display name", "Acceptance Test Reporting", this.publisher.getDescriptor().getDisplayName());
    }

    @Test
    public void testIsApplicable_forAnyProjectType() throws Exception {
        assertTrue("is applicable", this.publisher.getDescriptor().isApplicable(FreeStyleProject.class));
        assertTrue("is applicable", this.publisher.getDescriptor().isApplicable(MavenModuleSet.class));
    }

    @Test
    public void testGetHelpFile() throws Exception {
        assertEquals("help file", "/plugin/acceptance-report-plugin/help/help-globalConfig.html",
                this.publisher.getDescriptor().getHelpFile());
    }

    @Test
    public void testDoCheckReportBaseUrl_emptyValue() throws Exception {
        FormValidation formValidation = getPublisherDescriptor().doCheckReportBaseUrl("");
        assertTrue("there is an error", formValidation.kind.equals(FormValidation.Kind.ERROR));
    }

    @Test
    public void testDoCheckReportBaseUrl_notValidURL() throws Exception {
        FormValidation formValidation = getPublisherDescriptor().doCheckReportBaseUrl("wrong URL");
        assertTrue("there is an error", formValidation.kind.equals(FormValidation.Kind.ERROR));
    }

    @Test
    public void testDoCheckReportBaseUrl_validUrl() throws Exception {
        FormValidation formValidation = getPublisherDescriptor().doCheckReportBaseUrl("http://localhost:80");
        assertTrue("valid url", formValidation.kind.equals(FormValidation.Kind.OK));
    }

    public void testDoCheckPublishDirectory() throws Exception {

    }

    public void testDoCheckReportFileName() throws Exception {

    }

    public void testDoCheckLogPath() throws Exception {

    }

    private AcceptanceReportPublisherDescriptor getPublisherDescriptor() {
        return ((AcceptanceReportPublisherDescriptor) this.publisher.getDescriptor());
    }
}

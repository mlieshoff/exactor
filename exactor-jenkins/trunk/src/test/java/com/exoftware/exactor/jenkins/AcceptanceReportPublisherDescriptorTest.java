package com.exoftware.exactor.jenkins;


import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.util.FormValidation;
import org.junit.After;
import org.junit.Before;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Class description here.
 *
 * @author Andoni del Olmo
 * @changed: AdO 15.03.2012 - Added
 */
public class AcceptanceReportPublisherDescriptorTest extends HudsonTestCase {

    private AcceptanceReportPublisher publisher;
    private FreeStyleProject project;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.publisher = new AcceptanceReportPublisher();
        this.project = createFreeStyleProject();
        this.project.getPublishersList().add(this.publisher);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetDisplayName() throws Exception {
        assertEquals("display name", "Acceptance Test Reporting", this.publisher.getDescriptor().getDisplayName());
    }

    public void testIsApplicable_forAnyProjectType() throws Exception {
        assertTrue("is applicable", this.publisher.getDescriptor().isApplicable(FreeStyleProject.class));
        assertTrue("is applicable", this.publisher.getDescriptor().isApplicable(MavenModuleSet.class));
    }

    public void testGetHelpFile() throws Exception {
        assertEquals("help file", "/plugin/acceptance-report-plugin/help/help-globalConfig.html",
                this.publisher.getDescriptor().getHelpFile());
    }

    public void testDoCheckReportBaseUrl_emptyValue() throws Exception {
        FormValidation formValidation = getPublisherDescriptor().doCheckReportBaseUrl("");
        assertTrue("there is an error", formValidation.kind.equals(FormValidation.Kind.ERROR));
    }

    public void testDoCheckReportBaseUrl_notValidURL() throws Exception {
        FormValidation formValidation = getPublisherDescriptor().doCheckReportBaseUrl("wrong URL");
        assertTrue("there is an error", formValidation.kind.equals(FormValidation.Kind.ERROR));
    }

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

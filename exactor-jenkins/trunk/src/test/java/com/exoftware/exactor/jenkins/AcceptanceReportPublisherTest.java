package com.exoftware.exactor.jenkins;

import hudson.model.FreeStyleProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertEquals;

/**
 * @author Andoni del Olmo
 */
public class AcceptanceReportPublisherTest {

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
    public void testPerform() throws Exception {
        assertEquals("display name", "Acceptance Test Reporting", publisher.getDescriptor().getDisplayName());
    }

    @Test
    public void testNeedsToRunAfterFinalized() throws Exception {

    }

    @Test
    public void testGetRequiredMonitorService() throws Exception {

    }

    @Test
    public void testGetDescriptor() throws Exception {

    }
}

package com.exoftware.exactor.jenkins;

import hudson.model.FreeStyleProject;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Class description here.
 *
 * @author Andoni del Olmo
 * @changed: AdO 15.03.2012 - Added
 */
public class AcceptanceReportPublisherTest extends HudsonTestCase {

    private AcceptanceReportPublisher publisher;
    private FreeStyleProject project;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.publisher = new AcceptanceReportPublisher();
        this.project = createFreeStyleProject();
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

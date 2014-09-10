package com.exoftware.exactor.jenkins;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Andoni del Olmo
 * @changed: AdO 23.03.2012 - Added
 */
public class AcceptanceReportActionTest {

    private AcceptanceReportAction unitUnderTest;
    private AcceptanceReportActionFactory actionFactory;

    @Mock
    private URLChecker urlCheckerMock;

    @Before
    public void setUp() throws Exception {
        this.urlCheckerMock = mock(URLChecker.class);
        this.actionFactory = new AcceptanceReportActionFactory(this.urlCheckerMock);
    }

    @Test
    public void testDoesReportExists_true() throws Exception {
        when(this.urlCheckerMock.isUrlReachable("http://host/at_report_p1-265.html")).thenReturn(true);
        this.unitUnderTest = this.actionFactory.createAcceptanceReportAction();
        assertTrue("ACT Report exist", this.unitUnderTest.getDoesReportExists());
    }

    @Test
    public void testDoesReportExists_false() throws Exception {
        when(this.urlCheckerMock.isUrlReachable("http://host/at_report_p1-265.html")).thenReturn(false);
        this.unitUnderTest = this.actionFactory.createAcceptanceReportAction();
        assertFalse("ACT Report does not exist", this.unitUnderTest.getDoesReportExists());
    }

    @Test
    public void testGetReportURL_normal() throws Exception {
        this.unitUnderTest = this.actionFactory.createAcceptanceReportAction();
        assertEquals("ACT Report URL", "http://host/at_report_p1-265.html", this.unitUnderTest.getReportURL());
    }

    @Ignore
    @Test
    public void testGetLogURL_wrongReportBasePathURL() throws Exception {
        this.actionFactory.setReportBasePath("wrong URL");
        this.unitUnderTest = this.actionFactory.createAcceptanceReportAction();
        assertEquals("log URL", "http://host/at_out-265.log", this.unitUnderTest.getLogURL());
    }

    @Test
    public void testGetLogURL_normal() throws Exception {
        this.unitUnderTest = this.actionFactory.createAcceptanceReportAction();
        assertEquals("log URL", "http://host/at_out-265.log", this.unitUnderTest.getLogURL());
    }
}

class AcceptanceReportActionFactory {

    private URLChecker urlChecker;
    private String reportBasePath = "http://host/";
    private String reportFileName = "at_report_p1-{0}.html";
    private int buildVersion = 265;
    private String logPath = "\\tmp\\at_out.log";
    private String publishDirectory = "\\Program Files (x86)\\Apache Software Foundation\\Apache2.2\\htdocs\\";

    AcceptanceReportActionFactory(URLChecker urlChecker) {
        this.urlChecker = urlChecker;
    }

    public AcceptanceReportAction createAcceptanceReportAction() {
        AcceptanceReportAction acceptanceReportAction = new AcceptanceReportAction(this.reportBasePath, this.reportFileName, this.buildVersion,
                this.logPath, this.publishDirectory, "auth");
        acceptanceReportAction.setUrlChecker(this.urlChecker);
        return acceptanceReportAction;
    }

    public void setReportBasePath(String reportBasePath) {
        this.reportBasePath = reportBasePath;
    }

    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    public void setBuildVersion(int buildVersion) {
        this.buildVersion = buildVersion;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public void setPublishDirectory(String publishDirectory) {
        this.publishDirectory = publishDirectory;
    }
}

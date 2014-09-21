/*
 * JavaNetURLCheckerTest.java
 * 
 * 19.02.2014
 * 
 * (c) by Nicando Software GmbH
 */
package com.exoftware.exactor.jenkins;

import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * @author Andoni del Olmo
 */
@Ignore(value = "the urls must be mocked!!")
public class JavaNetURLCheckerTest {

    private JavaNetURLChecker unitUnderTest;

    @Test
    public void testIsUrlReachable() throws Exception {
        assertTrue("is reachable", unitUnderTest.isUrlReachable("http://www.google.com"));
    }

    @Test
    public void testIsUrlReachable_withAuth() throws Exception {
        unitUnderTest.setServerAuth(ServerAuth.getEncodedAuth("andy", "peng3$"));
        assertTrue("is reachable", unitUnderTest.isUrlReachable("https://bridge.abbino-cloud.de/acceptance"));
    }
}
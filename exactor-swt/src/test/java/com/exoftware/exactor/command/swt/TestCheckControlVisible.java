package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.command.swt.framework.TestSwt;
import junit.framework.ComparisonFailure;

import java.awt.*;

public class TestCheckControlVisible extends TestSwt {

    protected void setUp() throws Exception {
        super.setUp();
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        command = new CheckControlVisible();
        setScript();
    }

    public void testControlIsVisible() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        addParameter("txtTest");
        addParameter("true");
        try {
            command.execute();
            fail("should have thrown comparison exception");
        } catch (ComparisonFailure e) {
            assertEquals("wrong value expected:<tru...> but was:<fals...>", e.getMessage());
        }
    }

}

package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.command.swt.framework.TestSwt;
import junit.framework.ComparisonFailure;

import java.awt.*;

public class TestCheckControlEnabled extends TestSwt {

    protected void setUp() throws Exception {
        super.setUp();
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        command = new CheckControlEnabled();
        setScript();
    }

    public void testControlEnabled() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        command.addParameter(new Parameter("txtDisabled"));
        command.addParameter(new Parameter("true"));
        try {
            command.execute();
            fail("should have thrown comparison exception");
        } catch (ComparisonFailure e) {
            assertEquals("wrong value expected:<tru...> but was:<fals...>", e.getMessage());
        }

    }

}

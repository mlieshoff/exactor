package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.command.swt.framework.TestSwt;
import junit.framework.ComparisonFailure;

public class TestCheckControlEnabled extends TestSwt {
    protected void setUp() throws Exception {
        super.setUp();
        command = new CheckControlEnabled();
        setScript();
    }

    public void testControlEnabled() throws Exception {
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

package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.command.swt.framework.TestSwt;
import junit.framework.ComparisonFailure;

import java.awt.*;

public class TestCheckComboBoxEntries extends TestSwt {

    protected void setUp() throws Exception {
        super.setUp();
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        command = new CheckComboBoxEntries();
        setScript();
    }

    public void testComboBoxEntries() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        command.addParameter(new Parameter("cbTest"));
        command.addParameter(new Parameter("valueA,valueB"));
        try {
            command.execute();
            fail("should have thrown comparison exception");
        } catch (ComparisonFailure e) {
            assertEquals("wrong value expected:<valueA,valueB> but was:<>", e.getMessage());
        }
    }

}

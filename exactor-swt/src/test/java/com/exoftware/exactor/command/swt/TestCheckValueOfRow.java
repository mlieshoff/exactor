package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.command.swt.framework.TestSwt;

import java.awt.*;

public class TestCheckValueOfRow extends TestSwt {

    protected void setUp() throws Exception {
        super.setUp();
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        command = new CheckValueOfRow();
        setScript();
    }

    public void testValueOfRow() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        addParameter("tblTable(0)");
        addParameter("xx,xx");
    }

}

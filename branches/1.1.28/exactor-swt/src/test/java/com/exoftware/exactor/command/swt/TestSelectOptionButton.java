package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.command.swt.framework.TestSwt;


public class TestSelectOptionButton extends TestSwt {
    protected void setUp() throws Exception {
        super.setUp();
        command = new SelectOptionButton();
        setScript();
    }

    public void testSelect() throws Exception {
        assertFalse(testPanel.option1.getSelection());
        assertFalse(testPanel.option2.getSelection());
        assertFalse(testPanel.option3.getSelection());
        addParameter("optTest");
        addParameter("true");
        command.execute();
        assertTrue(testPanel.option1.getSelection());
        assertFalse(testPanel.option2.getSelection());
        assertFalse(testPanel.option3.getSelection());
        command = new SelectOptionButton();
        setScript();
        addParameter("optTest2");
        addParameter("true");
        command.execute();
        assertFalse(testPanel.option1.getSelection());
        assertFalse(testPanel.option2.getSelection());
        assertTrue(testPanel.option3.getSelection());
    }
}

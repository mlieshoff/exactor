package com.exoftware.exactor.command.swing;

import com.exoftware.exactor.Parameter;

public class TestExceptionOnComponentNotFound extends SwingTestCase {

    public void testFindUnknownControl() throws Exception {
        if (headless) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        ClickButton clickButton = new ClickButton();
        clickButton.addParameter(new Parameter("btnDoesNotExist"));
        clickButton.setScript(script);
        try {
            clickButton.doExecute();
            fail("runtime exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("Could not find component: btnDoesNotExist", e.getMessage());
        }
    }

}

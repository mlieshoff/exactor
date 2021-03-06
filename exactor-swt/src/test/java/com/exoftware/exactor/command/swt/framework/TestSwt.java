package com.exoftware.exactor.command.swt.framework;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.Script;
import com.exoftware.exactor.command.swt.CheckValueOfText;
import com.exoftware.exactor.command.swt.ClickButton;
import com.exoftware.exactor.command.swt.InputText;
import junit.framework.AssertionFailedError;
import junit.framework.ComparisonFailure;

import java.awt.*;

public class TestSwt extends GuiAbstractTest {
    public SwtTestPanel testPanel;
    protected Command command;
    private Script script;

    protected void setUp() throws Exception {
        super.setUp();
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        script = new Script();
        testPanel = new SwtTestPanel(shell);
        script.getContext().put("RootComposite", testPanel);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        testPanel.dispose();
    }

    public void testPushButton() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        command = new ClickButton();
        setScript();
        command.addParameter(new Parameter("btnTest"));
        command.execute();
        assertTrue(testPanel.testButtonPressed);
    }

    public void testInputsText() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        command = new InputText();
        setScript();
        command.addParameter(new Parameter("txtTest"));
        command.addParameter(new Parameter("XXX"));
        command.execute();
        assertEquals("XXX", testPanel.txtTest.getText());
    }

    public void testTestValueOfText() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        command = new CheckValueOfText();
        setScript();
        testPanel.txtTest.setText("XXX");
        command.addParameter(new Parameter("txtTest"));
        command.addParameter(new Parameter("XXX"));
        command.execute();
        command = new CheckValueOfText();
        setScript();
        command.addParameter(new Parameter("txtTest"));
        command.addParameter(new Parameter("YYY"));
        try {
            command.execute();
            fail("ComparisonFailure exception not thrown");
        } catch (ComparisonFailure e) {
            assertEquals("wrong text expected:<YYY> but was:<XXX>", e.getMessage());
        }
    }

    public void testNoRootComposite() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        command = new InputText();
        command.addParameter(new Parameter("txtTest"));
        command.addParameter(new Parameter("XXX"));
        try {
            command.execute();
            fail("ScriptExecutionException not thrown");
        } catch (AssertionFailedError e) {
            assertEquals("RootComposite has not been added to context", e.getMessage());
        }
    }

    public void testWidgetDoesNotExist() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        command = new InputText();
        setScript();
        command.addParameter(new Parameter("txtDoesNotExist"));
        command.addParameter(new Parameter("XXX"));
        try {
            command.execute();
            fail("ScriptExecutionException not thrown");
        } catch (AssertionFailedError e) {
            assertEquals("could not find control: txtDoesNotExist", e.getMessage());
        }
    }

    protected void setScript() {
        command.setScript(script);
    }

    protected void addParameter(String parameter) {
        command.addParameter(new Parameter(parameter));
    }
}

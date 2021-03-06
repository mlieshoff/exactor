package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import java.awt.*;

public abstract class ControlTextManagerAbstractTest extends GuiAbstractTest {

    Button button;
    Text textBox;
    Composite classWithNoGetTextMethod;

    protected abstract void assertControlText(String expectedText, Control control);

    protected void setUp() throws Exception {
        super.setUp();
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        button = new Button(shell, SWT.PUSH);
        textBox = new Text(shell, SWT.PUSH);
        classWithNoGetTextMethod = new Composite(shell, SWT.BORDER) {};
    }
}

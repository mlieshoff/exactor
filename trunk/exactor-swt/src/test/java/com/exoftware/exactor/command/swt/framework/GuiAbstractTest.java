package com.exoftware.exactor.command.swt.framework;

import junit.framework.TestCase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public abstract class GuiAbstractTest extends TestCase {
    protected Display display;
    protected Shell shell;

    protected void setUp() throws Exception {
        super.setUp();
        display = DisplayFactory.get();
        shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setSize(100, 50);
    }

    protected void tearDown() throws Exception {
        shell.dispose();
        DisplayFactory.destroy();
        super.tearDown();
    }

    protected void assertControlName(ControlName expectedControlName, Control control) {
        assertEquals(expectedControlName, ControlName.getControlName(control));
    }
}

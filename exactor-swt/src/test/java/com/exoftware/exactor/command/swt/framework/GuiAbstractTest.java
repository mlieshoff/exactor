package com.exoftware.exactor.command.swt.framework;

import junit.framework.TestCase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.awt.*;


public abstract class GuiAbstractTest extends TestCase {
    protected Display display;
    protected Shell shell;

    protected void setUp() throws Exception {
        super.setUp();
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        display = DisplayFactory.get();
        shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setSize(100, 50);
    }

    protected void tearDown() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        shell.dispose();
        DisplayFactory.destroy();
        super.tearDown();
    }

    protected void assertControlName(ControlName expectedControlName, Control control) {
        assertEquals(expectedControlName, ControlName.getControlName(control));
    }
}

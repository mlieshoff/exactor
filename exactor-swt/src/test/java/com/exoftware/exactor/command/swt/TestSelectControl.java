package com.exoftware.exactor.command.swt;

import com.exoftware.exactor.command.swt.framework.ControlName;
import com.exoftware.exactor.command.swt.framework.GuiEvent;
import com.exoftware.exactor.command.swt.framework.TestSwt;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class TestSelectControl extends TestSwt {
    private boolean selected;
    private Button button;

    protected void setUp() throws Exception {
        super.setUp();
        command = new SelectControl();
        setScript();
        selected = false;
        button = new Button(testPanel, SWT.PUSH);
        ControlName.setControlName(button, new ControlName("butTest"));
    }

    public void test() throws Exception {
        GuiEvent.addSelectionListener(button, new Listener() {
            public void handleEvent(Event event) {
                selected = true;
            }
        });
        addParameter("butTest");
        command.execute();
        assertTrue(selected);
    }
}

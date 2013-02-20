package com.exoftware.exactor.command.swing;

import javax.swing.*;

public class FrameTest extends JFrame {
    PanelTest panelTest = new PanelTest();

    public FrameTest(String title) {
        super(title);
        getContentPane().add(panelTest);
    }

    public static void main(String[] args) {
        FrameTest frameTest = new FrameTest("Test Window");
        frameTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Display the window.
        frameTest.pack();
        frameTest.setVisible(true);
    }
}

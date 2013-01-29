package com.exoftware.exactor.command.swing;

import javax.swing.JButton;
import javax.swing.JFrame;

public class AnotherTestFrame extends JFrame
{
    JButton jButton = new JButton( );
    public AnotherTestFrame( String title )
    {
        super( title );

        jButton.setName( "FindMe" );
        getContentPane().add( jButton );
    }

    public static void main( String[] args )
    {
        AnotherTestFrame frame = new AnotherTestFrame("Test Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}

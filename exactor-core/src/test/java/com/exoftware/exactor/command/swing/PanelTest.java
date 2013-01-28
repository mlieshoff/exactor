package com.exoftware.exactor.command.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelTest extends JPanel
{
    JButton button = new JButton( "TestButton" );
    JLabel findControl1 = new JLabel("1111111");
    JLabel findControl2 = new JLabel("2222222");
    JLabel findControl3 = new JLabel("3333333");
    JPanel nestedPanel = new JPanel();
    boolean buttonClicked = false;

    JLabel enabledVisible = new JLabel( "enabledVisible" );
    JLabel notEnabled = new JLabel( "notEnabled" );
    JLabel notVisible = new JLabel( "notVisible" );



    public PanelTest()
    {
        addControlsToFind();
        setUpTestButton();
        setUpEnableVisibleTestingComponents();
    }

    private void setUpEnableVisibleTestingComponents()
    {
        notVisible.setVisible( false);
        notEnabled.setEnabled( false );

        enabledVisible.setName( "enabledVisible" );
        notEnabled.setName( "notEnabled" );
        notVisible.setName( "notVisible" );

        add(enabledVisible);
        add(notVisible);
        add(notEnabled);
    }

    private void addControlsToFind()
    {
        findControl1.setName( "findControl1" );
        findControl2.setName( "findControl2" );
        findControl3.setName( "findControl3" );

        add( findControl1 );

        JPanel levelOnePanel = new JPanel();
        levelOnePanel.add( findControl2 );
        JPanel levelTwoPanel = new JPanel();
        levelTwoPanel.add( findControl3 );
        levelOnePanel.add( levelTwoPanel );

        add( levelOnePanel );
    }

    private void setUpTestButton()
    {
        button.setName( "btnTest" );
        add( button );
        button.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                buttonClicked = true;
            }
        } );
    }
}

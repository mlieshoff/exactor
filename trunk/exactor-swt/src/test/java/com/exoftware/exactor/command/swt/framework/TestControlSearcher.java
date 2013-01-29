package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class TestControlSearcher extends GuiAbstractTest
{
    private Composite topLevelComposite1;
    private Composite topLevelComposite2;
    private Composite topLevelComposite3;
    private Composite topLevelComposite4;
    private Text textBox;
    private ControlSearcher searcher;
    private Shell childShell;
    private Button button;
    private Button childShellButton;
    private Menu menuBar;
    private MenuItem subMenu;

    protected void setUp() throws Exception
    {
        super.setUp();
        topLevelComposite1 = new Composite( shell, SWT.NONE );
        topLevelComposite2 = new Composite( shell, SWT.NONE );
        topLevelComposite3 = new Composite( shell, SWT.NONE );
        topLevelComposite4 = new Composite( shell, SWT.NONE );
        menuBar = new Menu( shell, SWT.BAR );
        shell.setMenuBar( menuBar );

        Menu menuTopLevelItem = new Menu( menuBar );
        MenuItem menuItem = new MenuItem( menuBar, SWT.CASCADE );
        menuItem.setText( "File" );
        menuItem.setMenu( menuTopLevelItem );

        subMenu = new MenuItem( menuTopLevelItem, SWT.CASCADE );
        subMenu.setText( "Open File" );
        ControlName.setControlName( subMenu, new ControlName( "subMenu" ) );

        button = new Button( topLevelComposite1, SWT.PUSH );
        textBox = new Text( topLevelComposite1, SWT.BORDER );
        childShell = new Shell( shell, SWT.APPLICATION_MODAL );
        childShellButton = new Button( childShell, SWT.PUSH );

        ControlName.setControlName( topLevelComposite1, "composite1" );
        ControlName.setControlName( topLevelComposite2, "composite2" );
        ControlName.setControlName( topLevelComposite3, new ControlName( null ) );
        ControlName.setControlName( topLevelComposite4, "composite4" );
        ControlName.setControlName( button, "btnButton" );
        ControlName.setControlName( textBox, "txtTextBox" );
        ControlName.setControlName( childShell, "childShell" );
        ControlName.setControlName( childShellButton, "childShellButton" );
    }

    public void testDoesNotExist()
    {
        assertCompositeDoesNotExist( "abc" );
    }

    public void testCompositeDoesExist()
    {
        assertCompositeDoesExist( "composite1" );
        assertCompositeDoesExist( "composite2" );
        assertCompositeDoesExist( "composite4" );
    }

    public void testSearchForControl()
    {
        createSearcher( "txtTextBox" );
        assertSame( textBox, searcher.searchForControl() );
    }

    public void testSearchForShells()
    {
        assertSearchForControl( childShell, "childShell" );
        assertSearchForControl( childShellButton, "childShellButton" );
    }

    public void testSearchForMenuItem()
    {
        assertSearchForControl( subMenu, "subMenu" );
    }

    private void assertSearchForControl( Widget expectedWidget, String controlName )
    {
        createSearcher( controlName );
        assertSame( expectedWidget, searcher.searchForControl() );
    }

    private void assertCompositeDoesNotExist( String controlName )
    {
        assertCompositeExistence( controlName, false );
    }

    private void assertCompositeDoesExist( String controlName )
    {
        assertCompositeExistence( controlName, true );
    }

    private void assertCompositeExistence( String controlName, boolean expectedExists )
    {
        createSearcher( controlName );
        assertEquals( expectedExists, searcher.exists() );
    }

    private void createSearcher( String controlName )
    {
        searcher = new ControlSearcher( shell, new ControlName( controlName ) );
    }

}

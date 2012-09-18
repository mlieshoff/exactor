package gui;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Application
{
    private static final String APP_TITLE_TEXT = "Sample Application";
    private static final int APP_WIDTH = 320;
    private static final int APP_HEIGHT = 240;

    Display display;
    Shell shell;
    CalculatorPanel calculator;

    public Application( Display display )
    {
        this.display = display;
        setUp();
    }

    private void setUp()
    {
        addShell();
        addCalculatorPanel();
        openShell();
    }

    private void addCalculatorPanel()
    {
        calculator = new CalculatorPanel( shell );
    }

    private void openShell()
    {
        shell.open();
    }

    private void addShell()
    {
        shell = new Shell( display );
        shell.setText( APP_TITLE_TEXT );
        shell.setSize( APP_WIDTH, APP_HEIGHT );
        shell.setLayout( new FillLayout() );
    }

    public void dispose()
    {
        shell.dispose();
    }

    public Composite getShell()
    {
        return shell;
    }

    public boolean isDisposed()
    {
        return shell.isDisposed();
    }
}

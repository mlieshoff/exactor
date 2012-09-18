package main;

import gui.Application;
import org.eclipse.swt.widgets.Display;

public class Main
{
    public static void main( String[] args )
    {
        Display display = new Display();

        Application application = new Application( display );

        while( !application.isDisposed() )
            if( !display.readAndDispatch() )
                display.sleep();
        display.dispose();
    }

}


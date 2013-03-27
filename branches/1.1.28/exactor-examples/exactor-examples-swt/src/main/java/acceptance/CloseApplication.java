package acceptance;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.command.swt.framework.DisplayFactory;
import gui.Application;

public class CloseApplication extends Command {
    public void execute() throws Exception {
        Application application = (Application) getScript().getContext().get("Application");
        application.dispose();
        DisplayFactory.destroy();
    }
}

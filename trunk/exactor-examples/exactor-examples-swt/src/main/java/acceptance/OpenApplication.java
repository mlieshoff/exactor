package acceptance;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.command.swt.AbstractSwtCommand;
import com.exoftware.exactor.command.swt.framework.DisplayFactory;
import gui.Application;
import org.eclipse.swt.widgets.Composite;

public class OpenApplication extends Command {
    public void execute() throws Exception {
        Application application = new Application(DisplayFactory.get());
        Composite rootComposite = application.getShell();
        getScript().getContext().put("Application", application);
        getScript().getContext().put(AbstractSwtCommand.ROOT_SWT_SHELL, rootComposite);
    }
}

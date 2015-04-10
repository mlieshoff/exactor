Just define a simple command:

```
import com.exoftware.exactor.Command;

public class Break extends Command {

    @Override
    public void execute() throws Exception {
        System.out.println("Break here!");
    }
}
```

set a breakpoint in the line and use in your scripts started exactor in debugging mode.

```
MyCommand a=1
Break
MyOtherCommand b=42
```
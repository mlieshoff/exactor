package com.exoftware.exactor.command.swing;

import com.exoftware.exactor.Command;

import java.awt.*;

public class AbstractSwingCommand extends Command {
    protected static final String SWING_CONTAINER_KEYS = "Containers";

    /**
     * Searches an array of <code>java.awt.Container</code> objects for a named component. The name is found by
     * extracting the valueof the first parameter passed in.
     *
     * @return found component or null
     */
    //todo throw better exception
    protected Component findComponent() {
        String name = getParameter(0).stringValue();
        return findComponent(name);
    }

    public Component findComponent(String name) {
        Component component = ComponentFinder.findComponent(getContainers(), name);
        if (component == null) {
            throw new RuntimeException("Could not find component: " + name);
        } else {
            return component;
        }
    }

    protected Container[] getContainers() {
        return (Container[]) getScript().getContext().get(SWING_CONTAINER_KEYS);
    }
}

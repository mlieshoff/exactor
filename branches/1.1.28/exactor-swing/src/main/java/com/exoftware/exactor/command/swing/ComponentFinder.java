package com.exoftware.exactor.command.swing;

import java.awt.*;

/**
 * Utility class for searching an array of <code>java.awt.Container</code> objects for a named component.
 *
 * @author Sean Hanly
 */
public class ComponentFinder {
    /**
     * Finds the component by checking the actual componet passed to see if it has the right name and if not it checks
     * see if it is a container and then searches this, otherwise it returns null.
     *
     * @param component component to search
     * @param name      component name
     * @return found component or null
     */
    public static Component findComponent(Component component, String name) {
        if ((component.getName() != null) && (component.getName().equals(name))) {
            return component;
        } else if (component instanceof Container) {
            Component foundComponent = findComponent(((Container) component).getComponents(), name);
            if (foundComponent == null) {
                if (component instanceof Window) {
                    Window window = (Window) component;
                    Window[] ownedWindows = window.getOwnedWindows();
                    for (int i = 0; i < ownedWindows.length; i++) {
                        Window ownedWindow = ownedWindows[i];
                        if ((ownedWindow != null) && (findComponent(ownedWindow, name) != null)) {
                            return findComponent(ownedWindow, name);
                        }
                    }
                }
            }
            return foundComponent;
        } else {
            return null;
        }
    }

    /**
     * Searches an array of <code>java.awt.Container</code> objects for a named component.
     *
     * @param components component array to search
     * @param name       component name
     * @return found       component or null
     */
    public static Component findComponent(Component[] components, String name) {
        for (int i = 0; i < components.length; i++) {
            if (findComponent(components[i], name) != null) {
                return findComponent(components[i], name);
            }
        }
        return null;
    }
}

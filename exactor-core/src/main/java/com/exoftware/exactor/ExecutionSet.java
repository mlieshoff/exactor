/******************************************************************
 * Copyright (c) 2004, Exoftware
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *   * Neither the name of the Exoftware, Exactor nor the names
 *     of its contributors may be used to endorse or promote
 *     products derived from this software without specific
 *     prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************/
package com.exoftware.exactor;

import com.exoftware.exactor.command.Composite;
import com.exoftware.exactor.command.utility.Verb;
import com.exoftware.exactor.parser.ScriptParser;
import com.exoftware.util.ClassFinder;
import com.exoftware.util.FileCollector;
import com.exoftware.util.Require;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A collection of scripts to be executed together.
 * ExecutionSetListeners can be added to the ExecutionSet to be notified of
 * script and command events.
 * The ExecutionSet contains a global context for all scripts it contains.
 *
 * @author Brian Swan
 */
public class ExecutionSet {
    public static final String SCRIPT_EXTENSION = ".act";

    private static final File USER_DIR = new File(System.getProperty("user.dir"));
    private static final String COMPOSITE_EXTENSION = ".cmp"; // to maintain backward compatibility

    private final Map<Object, Object> context = new HashMap<>();
    private final List<Script> scripts = new ArrayList<>();
    private final List<ExecutionSetListener> listeners = new ArrayList<>();
    private final Map<String, Class> commands = new HashMap<>();
    private final Map<String, Verb> verbs = new HashMap<>();
    private final Map<String, File> compositeScripts = new HashMap<>();

    private final Set<String> blacklistedClasses = new HashSet<>();

    private final String classpath;

    public ExecutionSet() {
        context.putAll(System.getProperties());
        Object blacklist = context.get("exactor.blacklisted.classes");
        if (blacklist != null) {
            String[] array = blacklist.toString().split("[,]");
            blacklistedClasses.addAll(Arrays.asList(array));
        }
        classpath = getClasspath();
    }

    private String getClasspath() {
        String classpath = System.getProperty("exactor.class.path");
        if (StringUtils.isBlank(classpath)) {
            classpath = System.getProperty("java.class.path");
        }
        return classpath;
    }

    /**
     * Returns the context for all scripts in this ExecutionSet.
     * This context map also contains all of the entries from <code>System.getProperties()</code>.
     *
     * @return the context for all scripts in this ExecutionSet.
     * @see System#getProperties()
     */
    public Map getContext() {
        return context;
    }

    /**
     * Add a script to the ExecutionSet.
     *
     * @param s the script to add.
     */
    public void addScript(Script s) {
        Require.condition(s != null, "Script cannot be null");
        s.setExecutionSet(this);
        scripts.add(s);
    }

    /**
     * Add a listener to the ExecutionSet.
     *
     * @param listener the listener to add.
     */
    public void addListener(ExecutionSetListener listener) {
        Require.condition(listener != null, "Listener cannot be null");
        listeners.add(listener);
    }

    /**
     * Execute all the scripts in the ExecutionSet.
     */
    public void execute() {
        fireExecutionSetStarted();
        for (Iterator i = scripts.iterator(); i.hasNext(); ) {
            ((Script) i.next()).execute();
        }
        fireExecutionSetEnded();
    }

    /**
     * Notify all added listeners that the execution set has started.
     */
    public void fireExecutionSetStarted() {
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            ((ExecutionSetListener) i.next()).executionSetStarted(this);
        }
    }

    /**
     * Notify all added listeners that the execution set has ended.
     */
    public void fireExecutionSetEnded() {
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            ((ExecutionSetListener) i.next()).executionSetEnded(this);
        }
    }

    /**
     * Notify all added listeners that the specified script has started.
     *
     * @param s the started script.
     */
    public void fireScriptStarted(Script s) {
        Require.condition(s != null, "Script cannot be null");
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            ((ExecutionSetListener) i.next()).scriptStarted(s);
        }
    }

    /**
     * Notify all added listeners that the specified script has ended.
     *
     * @param s the ended script.
     */
    public void fireScriptEnded(Script s) {
        Require.condition(s != null, "Script cannot be null");
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            ((ExecutionSetListener) i.next()).scriptEnded(s);
        }
    }

    /**
     * Notify all added listeners that the specified command has started.
     *
     * @param c the started command.
     */
    public void fireCommandStarted(Command c) {
        Require.condition(c != null, "Command cannot be null");
        c.setStartTime(System.currentTimeMillis());
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            ((ExecutionSetListener) i.next()).commandStarted(c);
        }
    }

    /**
     * Notify all added listeners that the specified command has ended, possibly in error.
     *
     * @param c the ended command
     * @param t the reason the commanded ended or <code>null</code>.
     */
    public void fireCommandEnded(Command c, Throwable t) {
        Require.condition(c != null, "Command cannot be null");
        c.setEndTime(System.currentTimeMillis());
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            ((ExecutionSetListener) i.next()).commandEnded(c, t);
        }
    }

    /**
     * Find a <code>Command</code> with the specified name.
     *
     * @param name the name of the <code>Command</code> to find.
     * @return A <code>Command</code> for the specified name, or <code>null</code> if no command
     *         could be found.
     */
    public Command findCommand(String name) {
        if (commands.containsKey(name)) {
            return createCommandInstance(commands.get(name));
        }
        if (compositeScripts.containsKey(name)) {
            return createCompositeInstance(compositeScripts.get(name));
        }
        Command result = findCommandClass(name);
        if (result == null) {
            result = findComposite(name);
        }
        return result;
    }

    /**
     * Add a mapping of a command class to the specified <code>name</code>.
     *
     * @param name the name to use to reference this command in a script
     * @param c    the command class to execute for the supplied name.
     * @throws RuntimeException if <code>name</code> or <code>c</code> are <code>null</code>, or
     *                          if <code>c</code> is not a <code>Command</code> class.
     */
    public void addCommandMapping(String name, Class c) throws RuntimeException {
        Require.condition(name != null, "Command name cannot be null");
        Require.condition(c != null, "Command class cannot be null");
        Require.condition(Command.class.isAssignableFrom(c), "Class is not a Command or Command subclass");
        commands.put(name, c);
    }

    /**
     * Add a mapping of a composite scriptFile to the specified <code>name</code>.
     *
     * @param name       the name to use to reference this command in a scriptFile
     * @param scriptFile the scriptFile to execute for the suppied name.
     * @throws RuntimeException if <code>name</code> or <code>scriptFile</code> are <code>null</code>.
     */
    public void addCompositeMapping(String name, File scriptFile) {
        Require.condition(name != null, "Composite name cannot be null");
        Require.condition(scriptFile != null, "Composite script cannot be null");
        compositeScripts.put(name, scriptFile);
    }

    /**
     * Gets a verb specified by name.
     *
     * @param verbName verbs name
     * @return verb specified by name
     */
    public Verb getVerb(String verbName) {
        Require.condition(verbName != null, "Verb name cannot be null");
        Require.condition(verbName.length() > 0, "Verb name cannot be empty");
        Verb verb = verbs.get(verbName);
        Require.condition(verb != null, "Verb not existent");
        return verb;
    }

    /**
     * Registers a verb by verb name.
     *
     * @param verbName verbs name
     * @param verb verb instance
     */
    public void registerVerb(String verbName, Verb verb) {
        verbs.put(verbName, verb);
    }

    private Command findComposite(String name) {
        File f = findFirstMatchingInUserDir(name + COMPOSITE_EXTENSION);  // backwards compatibility
        if (f == null) {
            f = findFirstMatchingInClasspath(name + SCRIPT_EXTENSION);
        }
        if (f == null) {
            return null;
        }
        compositeScripts.put(name, f);
        return createCompositeInstance(f);
    }

    private File findFirstMatchingInClasspath(String name) {
        File[] matches = FileCollector.filesWithName(classpath, name);
        if (matches.length > 0) {
            return matches[0];
        }
        return null;
    }

    private File findFirstMatchingInUserDir(String name) {
        File[] matches = FileCollector.filesWithName(USER_DIR, name);
        if (matches.length > 0) {
            return matches[0];
        }
        return null;
    }

    private Command findCommandClass(String name) {
        Command result = createCommandInstance(ClassFinder.findClass(name, classpath));
        if (result != null) {
            addCommandMapping(name, result.getClass());
        }
        return result;
    }

    private Command createCommandInstance(Class c) {
        if (c != null) {
            try {
                if (!isBlacklisted(c)) {
                    Object result = c.newInstance();
                    if (result instanceof Command) {
                        return (Command) result;
                    }
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return null;
    }

    private boolean isBlacklisted(Class c) {
        return blacklistedClasses.contains(c.getName());
    }

    private Command createCompositeInstance(File f) {
        Script s = new ScriptParser(this).parse(f);
        return new Composite(s);
    }

}

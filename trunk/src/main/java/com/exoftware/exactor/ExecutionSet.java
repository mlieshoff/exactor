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
import com.exoftware.exactor.parser.ScriptParser;
import com.exoftware.util.ClassFinder;
import com.exoftware.util.FileCollector;
import com.exoftware.util.Require;

import java.io.File;
import java.util.*;

/**
 * A collection of scripts to be executed together.
 * ExecutionSetListeners can be added to the ExecutionSet to be notified of
 * script and command events.
 * The ExecutionSet contains a global context for all scripts it contains.
 *
 * @author Brian Swan
 */
public class ExecutionSet
{
    public static final String SCRIPT_EXTENSION = ".act";
    private static final File USER_DIR = new File( System.getProperty( "user.dir" ) );
    private static final String CLASSPATH = System.getProperty( "java.class.path" );
    private static final String COMPOSITE_EXTENSION = ".cmp"; // to maintain backward compatibility

    private final Map context = new HashMap();
    private final List scripts = new ArrayList();
    private final List listeners = new ArrayList();
    private final Map commands = new HashMap();
    private final Map compositeScripts = new HashMap();

    public ExecutionSet()
    {
        context.putAll( System.getProperties() );
    }

    /**
     * Returns the context for all scripts in this ExecutionSet.
     * This context map also contains all of the entries from <code>System.getProperties()</code>.
     *
     * @return the context for all scripts in this ExecutionSet.
     *
     * @see System#getProperties()
     */
    public Map getContext()
    {
        return context;
    }

    /**
     * Add a script to the ExecutionSet.
     *
     * @param s the script to add.
     */
    public void addScript( Script s )
    {
        Require.condition( s != null, "Script cannot be null" );

        s.setExecutionSet( this );
        scripts.add( s );
    }

    /**
     * Add a listener to the ExecutionSet.
     *
     * @param listener the listener to add.
     */
    public void addListener( ExecutionSetListener listener )
    {
        Require.condition( listener != null, "Listener cannot be null" );

        listeners.add( listener );
    }

    /**
     * Execute all the scripts in the ExecutionSet.
     */
    public void execute()
    {
        fireExecutionSetStarted();
        for( Iterator i = scripts.iterator(); i.hasNext(); )
            ((Script) i.next()).execute();
        fireExecutionSetEnded();
    }

    /**
     * Notify all added listeners that the execution set has started.
     */
    public void fireExecutionSetStarted()
    {
        for( Iterator i = listeners.iterator(); i.hasNext(); )
            ((ExecutionSetListener) i.next()).executionSetStarted( this );
    }

    /**
     * Notify all added listeners that the execution set has ended.
     */
    public void fireExecutionSetEnded()
    {
        for( Iterator i = listeners.iterator(); i.hasNext(); )
            ((ExecutionSetListener) i.next()).executionSetEnded( this );
    }

    /**
     * Notify all added listeners that the specified script has started.
     *
     * @param s the started script.
     */
    public void fireScriptStarted( Script s )
    {
        Require.condition( s != null, "Script cannot be null" );

        for( Iterator i = listeners.iterator(); i.hasNext(); )
            ((ExecutionSetListener) i.next()).scriptStarted( s );
    }

    /**
     * Notify all added listeners that the specified script has ended.
     *
     * @param s the ended script.
     */
    public void fireScriptEnded( Script s )
    {
        Require.condition( s != null, "Script cannot be null" );

        for( Iterator i = listeners.iterator(); i.hasNext(); )
            ((ExecutionSetListener) i.next()).scriptEnded( s );
    }

    /**
     * Notify all added listeners that the specified command has started.
     *
     * @param c the started command.
     */
    public void fireCommandStarted( Command c )
    {
        Require.condition( c != null, "Command cannot be null" );

        for( Iterator i = listeners.iterator(); i.hasNext(); )
            ((ExecutionSetListener) i.next()).commandStarted( c );
    }

    /**
     * Notify all added listeners that the specified command has ended, possibly in error.
     *
     * @param c the ended command
     * @param t the reason the commanded ended or <code>null</code>.
     */
    public void fireCommandEnded( Command c, Throwable t )
    {
        Require.condition( c != null, "Command cannot be null" );

        for( Iterator i = listeners.iterator(); i.hasNext(); )
            ((ExecutionSetListener) i.next()).commandEnded( c, t );
    }

    /**
     * Find a <code>Command</code> with the specified name.
     *
     * @param name the name of the <code>Command</code> to find.
     *
     * @return A <code>Command</code> for the specified name, or <code>null</code> if no command
     *         could be found.
     */
    public Command findCommand( String name )
    {
        if( commands.containsKey( name ) )
            return createCommandInstance( (Class) commands.get( name ) );
        if( compositeScripts.containsKey( name ) )
            return createCompositeInstance( (File) compositeScripts.get( name ) );

        Command result = findCommandClass( name );
        if( result == null )
            result = findComposite( name );

        return result;
    }

    /**
     * Add a mapping of a command class to the specified <code>name</code>.
     *
     * @param name the name to use to reference this command in a script
     * @param c    the command class to execute for the supplied name.
     *
     * @throws RuntimeException if <code>name</code> or <code>c</code> are <code>null</code>, or
     *                          if <code>c</code> is not a <code>Command</code> class.
     */
    public void addCommandMapping( String name, Class c ) throws RuntimeException
    {
        Require.condition( name != null, "Command name cannot be null" );
        Require.condition( c != null, "Command class cannot be null" );
        Require.condition( Command.class.isAssignableFrom( c ), "Class is not a Command or Command subclass" );

        commands.put( name, c );
    }

    /**
     * Add a mapping of a composite scriptFile to the specified <code>name</code>.
     *
     * @param name   the name to use to reference this command in a scriptFile
     * @param scriptFile the scriptFile to execute for the suppied name.
     *
     * @throws RuntimeException if <code>name</code> or <code>scriptFile</code> are <code>null</code>.
     */
    public void addCompositeMapping( String name, File scriptFile )
    {
        Require.condition( name != null, "Composite name cannot be null" );
        Require.condition( scriptFile != null, "Composite script cannot be null" );

        compositeScripts.put( name, scriptFile );
    }

    private Command findComposite( String name )
    {
        File f = findFirstMatchingInUserDir( name + COMPOSITE_EXTENSION );  // backwards compatibility
        if( f == null )
            f = findFirstMatchingInClasspath( name + SCRIPT_EXTENSION );

        if( f == null )
            return null;

        compositeScripts.put( name, f );

        return createCompositeInstance( f );
    }

    private File findFirstMatchingInClasspath( String name )
    {
        File[] matches = FileCollector.filesWithName( CLASSPATH, name );
        if( matches.length > 0 )
            return matches[0];

        return null;
    }

    private File findFirstMatchingInUserDir( String name )
    {
        File[] matches = FileCollector.filesWithName( USER_DIR, name );
        if( matches.length > 0 )
            return matches[0];

        return null;
    }

    private Command findCommandClass( String name )
    {
        Command result = createCommandInstance( ClassFinder.findClass( name, CLASSPATH ) );
        if( result != null )
            addCommandMapping( name, result.getClass() );

        return result;
    }

    private Command createCommandInstance( Class c )
    {
        try
        {
            Object result = c.newInstance();
            if( result instanceof Command )
            {
                return (Command) result;
            }
        }
        catch( Exception ignored )
        {
        }
        return null;
    }

    private Command createCompositeInstance( File f )
    {
        Script s = new ScriptParser( this ).parse( f );
        return new Composite( s );
    }
}

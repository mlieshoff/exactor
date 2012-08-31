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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of commands to be executed together.
 * The script contains a context map that is accessible to all
 * the commands contained in the script.
 *
 * @author Brian Swan
 */
public class Script
{
    private static final String DEFAULT_NAME = "DEFAULT";

    private final String name;
    private final String absolutePath;
    private final Map context = new HashMap();
    private final List commands = new ArrayList();
    private ExecutionSet executionSet = new ExecutionSet();

    /**
     * Create a new script.
     */
    public Script()
    {
        this.name = DEFAULT_NAME;
        this.absolutePath = "";
    }

    /**
     * Create a new script with the specified scriptFile.
     *
     * @param scriptFile the file containing the script.
     */
    public Script( File scriptFile )
    {
        if( scriptFile == null )
            throw new RuntimeException( "Name cannot be null" );

        this.name = scriptFile.getName();
        this.absolutePath = scriptFile.getAbsolutePath();
    }

    /**
     * Returns the name of the script.
     *
     * @return the name of the script.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the full path of the script file.
     *
     * @return the full path of the script file.
     */
    public String getAbsolutePath()
    {
        return absolutePath;
    }

    /**
     * Returns the ExecutionSet containing this script.
     *
     * @return the ExecutionSet containing this script.
     */
    public ExecutionSet getExecutionSet()
    {
        return executionSet;
    }

    /**
     * Set the ExecutionSet that contains this script.
     *
     * @param s the ExecutionSet that contains this script.
     */
    public void setExecutionSet( ExecutionSet s )
    {
        if( s == null )
            throw new RuntimeException( "ExecutionSet cannot be null" );

        executionSet = s;
    }

    /**
     * Returns the context for all commands in this script.
     *
     * @return the context for all commands in this script.
     */
    public Map getContext()
    {
        return context;
    }

    /**
     * Add a command to the script.
     *
     * @param c the command to add.
     */
    public void addCommand( Command c )
    {
        if( c == null )
            throw new RuntimeException( "Command cannot be null" );

        c.setScript( this );
        commands.add( c );
    }

    /**
     * Check whether the script has any commands.
     *
     * @return <code>true</code> if the script contains commands,
     *         otherwise <code>false</code>.
     */
    public boolean hasCommands()
    {
        return countCommands() > 0;
    }

    /**
     * Returns the number of commands added to the script.
     *
     * @return the number of commands added to the script.
     */
    public int countCommands()
    {
        return commands.size();
    }

    /**
     * Returns the command at the specified index.
     *
     * @param index the index of the command to return.
     * @return the command at the specified index.
     * @throws IndexOutOfBoundsException if the command has no parameters, or
     *                                   if the index is out of range (index < 0 || index >= countCommands()).
     */
    public Command getCommand( int index ) throws IndexOutOfBoundsException
    {
        Command result;
        try
        {
            result = (Command) commands.get( index );
        }
        catch( IndexOutOfBoundsException e )
        {
            throw new IndexOutOfBoundsException( "Index: " + index + ", Size: " + commands.size() );  // this is done for consistancy across JDK versions.
        }
        return result;
    }

    /**
     * Replace one command with another command.
     *
     * @param command     the command to replace.
     * @param replacement the replacement command.
     * @throws RuntimeException if either command or replacement are <code>null</code> or
     *                          if command is not contained in this script
     */
    public void replaceCommand( Command command, Command replacement ) throws NullPointerException, IllegalStateException
    {
        if( command == null )
            throw new RuntimeException( "Command cannot be null" );
        if( replacement == null )
            throw new RuntimeException( "Replacement cannot be null" );
        if( !commands.contains( command ) )
            throw new RuntimeException( "Command does not exist in script" );

        replacement.setScript( this );
        int index = commands.indexOf( command );
        commands.add( index, replacement );
        commands.remove( index + 1 );
    }

    /**
     * Execute all the commands in the script.
     */
    public void execute()
    {
        executionSet.fireScriptStarted( this );
        for( int i = 0; i < countCommands(); i++ )
        {
            Command c = getCommand( i );
            Throwable throwable = null;
            getExecutionSet().fireCommandStarted( c );
            try
            {
                c.execute();
            }
            catch( Throwable t )
            {
                throwable = t;
            }
            getExecutionSet().fireCommandEnded( c, throwable );
        }

        executionSet.fireScriptEnded( this );
    }

    /**
     * Substitute the specified <code>substitutions</code> for all
     * commands in the script.
     *
     * @param substitutions the array of parameters to use for substitutions.
     *
     * @see Command#substituteParameters(Parameter[]) 
     */
    public void substituteParameters( Parameter[] substitutions )
    {
        if( substitutions == null )
            throw new RuntimeException( "Substitutions cannot be null" );

        for( int i = 0; i < countCommands(); i++ )
            getCommand( i ).substituteParameters( substitutions );
    }
}

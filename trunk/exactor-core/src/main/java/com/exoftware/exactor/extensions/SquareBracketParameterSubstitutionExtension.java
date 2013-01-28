package com.exoftware.exactor.extensions;

import com.exoftware.exactor.*;
import com.exoftware.exactor.command.Composite;

/**
 * See method comment on <code> public void commandStarted( Command c )</code>.
 */

public class SquareBracketParameterSubstitutionExtension implements ExecutionSetListener
{
    public void executionSetStarted( ExecutionSet es )
    {

    }

    public void executionSetEnded( ExecutionSet es )
    {

    }

    public void scriptStarted( Script s )
    {

    }

    public void scriptEnded( Script s )
    {

    }

    /**
     * Copy all of the poistional paramters into the script context to ensure they are available for substitution of
     * the format [position] e.g. ACommand [0]. Used for parameter substition in composites as an alternative to the
     * $position style.
     *
     * @param c command about to be executed
     */
    public void commandStarted( Command c )
    {
        if ( c instanceof Composite )
            copyCompositeParametersIntoScriptContext( c );
    }

    private void copyCompositeParametersIntoScriptContext( Command c )
    {
        Parameter[] parameters = c.getParameters();

        for ( int i = 0; i < parameters.length; i++ )
        {
            Parameter parameter = parameters[i];

            c.getScript().getContext().put( "" + i, parameter.stringValue() );
        }
    }

    public void commandEnded( Command c, Throwable t )
    {
        //todo remove params??
    }
}
package com.exoftware.exactor.command.utility;

import com.exoftware.exactor.Command;

/**
 * Sets a property in the script context. For example:
 *
 * <code>
 * # sets property name
 * SetProperty name john
 *
 * # print "hello john"
 * Print "hello [name]"
 * </code>
 */
public class SetProperty extends Command
{
    public void execute() throws Exception
    {
        getScript().getContext().put( getParameter( 0 ).stringValue(),
                                      getParameter( 1 ).stringValue() );
    }
}

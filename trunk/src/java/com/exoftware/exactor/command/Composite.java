package com.exoftware.exactor.command;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Script;
import junit.framework.AssertionFailedError;

import java.io.File;

/**
 * Represents a composite command.
 *
 */
public class Composite extends Command
{
    private final Script script;

    public Composite( Script s )
    {
        script = s;
    }

    public void execute() throws Exception
    {
        script.substituteParameters( getParameters() );
        for( int i = 0; i < script.countCommands(); i++ )
        {
            Command c = script.getCommand( i );
            c.setScript( getScript() );
            getScript().getExecutionSet().fireCommandStarted( c );
            try
            {
                c.execute();
            }
            catch( AssertionFailedError e )
            {
                getScript().getExecutionSet().fireCommandEnded( c, e );
                throw e;
            }
            catch( Exception e )
            {
                getScript().getExecutionSet().fireCommandEnded( c, e );
                throw e;
            }
            getScript().getExecutionSet().fireCommandEnded( c, null );
        }
    }

    public Script getCompositeScript()
    {
        return script;
    }

    public File getCompositeScriptFile()
    {
        return new File( script.getAbsolutePath() );
    }
}

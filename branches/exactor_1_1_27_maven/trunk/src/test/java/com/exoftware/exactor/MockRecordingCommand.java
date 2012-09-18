package com.exoftware.exactor;

/**
 * Stub implementation of <code>Command</code>
 * for testing. It records call count and parameters passed
 *
 * @author Sean Hanly
 */
public class MockRecordingCommand extends Command
{
    public static int staticExecuteCalledCount = 0;
    public static String recordedParameters = "";

    public void execute() throws Exception
    {
        Parameter[] parameters = getParameters();

        for( int i = 0; i < parameters.length; i++ )
        {
            Parameter parameter = parameters[i];
            recordedParameters += parameter.stringValue() + " ";
        }
        recordedParameters += "\n";

        staticExecuteCalledCount++;
    }
}

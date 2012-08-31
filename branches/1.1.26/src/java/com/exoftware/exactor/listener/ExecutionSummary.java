/******************************************************************
 * Copyright (c) 2005, Exoftware
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
package com.exoftware.exactor.listener;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.Script;
import com.exoftware.util.ClassFinder;
import junit.framework.AssertionFailedError;

import java.util.*;

/**
 * ExecutionSetListener that records statistics about the
 * current execution.
 *
 * @author Brian Swan
 */
public class ExecutionSummary
{
    private int runCount;
    private int failureCount;
    private int errorCount;
    private String packageBase;
    private long startTime;
    private Map packageSummaries = new LinkedHashMap();
    private PackageSummary currentSummary;

    public ExecutionSummary()
    {
        this( System.getProperty( "user.dir" ) );
    }

    public ExecutionSummary( String packageBase )
    {
        this.packageBase = packageBase;
    }

    public void executionStarted()
    {
        startTime = getCurrentTime();
    }

    public void scriptStarted( Script s )
    {
        String packageName = packageForScript( s, packageBase );
        if( !packageSummaries.containsKey( packageName ) )
            packageSummaries.put( packageName, new PackageSummary( packageName ) );

        currentSummary = (PackageSummary) packageSummaries.get( packageName );
        currentSummary.scriptStarted( s );
        runCount++;
    }

    public void commandEnded( Command c, Throwable t )
    {
        if( t instanceof AssertionFailedError )
            failureCount++;
        else if( t != null )
            errorCount++;

        if( currentSummary != null )
            currentSummary.commandEnded( c, t );
    }

    public int getScriptsRunCount()
    {
        return runCount;
    }

    public int getFailureCount()
    {
        return failureCount;
    }

    public int getErrorCount()
    {
        return errorCount;
    }

    public long getElapsedTimeSeconds()
    {
        return (getCurrentTime() - startTime) / 1000;
    }

    public PackageSummary[] getPackageSummaries()
    {
        return (PackageSummary[]) packageSummaries.values().toArray( new PackageSummary[packageSummaries.size()] );
    }

    protected long getCurrentTime()
    {
        return System.currentTimeMillis();
    }

    private String packageForScript( Script script, String packageBase )
    {
        String scriptPath = script.getAbsolutePath();
        String scriptDir = scriptPath.substring( 0, scriptPath.length() - script.getName().length() );
        String result = ClassFinder.toPackageName( scriptDir.substring( packageBase.length() ) );
        if( result.endsWith( ClassFinder.PACKAGE_SEPARATOR ) )
            return result.substring( 0, result.length() - 1 );

        return result;
    }
}

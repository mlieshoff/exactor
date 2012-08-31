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
import com.exoftware.util.FileUtilities;

import java.io.File;

/**
 * @author Brian Swan
 */
public class ScriptSummary
{
    private final String name;
    private final String absolutePath;
    private final LineSummary[] lineSummaries;
    private boolean passed = true;

    public ScriptSummary( File scriptFile )
    {
        this.name = removeExtension( scriptFile );
        this.absolutePath = scriptFile.getAbsolutePath();
        this.lineSummaries = createLineSummaries( FileUtilities.linesFromFile( scriptFile ) );
    }

    private LineSummary[] createLineSummaries( String[] lines )
    {
        LineSummary[] result = new LineSummary[lines.length];
        for( int i = 0; i < lines.length; i++ )
            result[i] = new LineSummary( lines[i] );

        return result;
    }

    private String removeExtension( File scriptFile )
    {
        String name = scriptFile.getName();
        int endIndex = name.lastIndexOf( "." );
        return endIndex == -1 ? name : name.substring( 0, endIndex );
    }

    public String getName()
    {
        return name;
    }

    public String getAbsolutePath()
    {
        return absolutePath;
    }

    public boolean hasPassed()
    {
        return passed;
    }

    public LineSummary[] getLineSummaries()
    {
        return lineSummaries;
    }

    public void commandEnded( Command command, Throwable throwable )
    {
        passed &= throwable == null;
        if( command.getLineNumber() > 0 && command.getLineNumber() <= lineSummaries.length )
            lineSummaries[command.getLineNumber() - 1].commandEnded( throwable );
    }
}

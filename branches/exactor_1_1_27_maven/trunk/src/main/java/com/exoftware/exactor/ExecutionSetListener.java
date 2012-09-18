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

/**
 * A listener for script execution events.
 *
 * @author Brian Swan
 */
public interface ExecutionSetListener
{
    /**
     * Notify the listener that the executionSet has started.
     *
     * @param es the started execution set.
     */
    void executionSetStarted( ExecutionSet es );

    /**
     * Notify the listener that the executionSet has ended.
     *
     * @param es the ended execution set.
     */
    void executionSetEnded( ExecutionSet es );

    /**
     * Notify the listener that a script has started.
     *
     * @param s the started script.
     */
    void scriptStarted( Script s );

    /**
     * Notify the listener that a script has ended.
     *
     * @param s the ended script.
     */
    void scriptEnded( Script s );

    /**
     * Notify the listener that a command has started.
     *
     * @param c the started command.
     */
    void commandStarted( Command c );

    /**
     * Notify the listener that a command has ended, possibly in error.
     * If the command ended in error the supplied <code>Throwable</code>
     * will be non null. If <code>t</code> is an instance of <code>AssertionFailedError</code>
     * it indicates that the command perfomed as expected but a check was unsuccessful, otherwise <code>t</code>
     * indicates the reason the command ended.
     *
     * @param c the ended command
     * @param t the reason the command ended, or <code>null</code>.
     */
    void commandEnded( Command c, Throwable t );
}

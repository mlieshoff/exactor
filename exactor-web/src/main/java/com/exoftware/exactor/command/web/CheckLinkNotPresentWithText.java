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
package com.exoftware.exactor.command.web;

/**
 * Command wrapping jwebunit <code>WebTester.assertLinkNotPresentWithText( String, int }</code>
 * and <code>WebTester.assertLinkNotPresentWithText( String }.
 * <p/>
 * Usage example;
 * <pre>
 *  <code>
 *      CheckLinkNotPresentWithText linkText [index]
 *  </code>
 * </pre>
 *
 * @author Brian Swan
 */
public class CheckLinkNotPresentWithText extends WebCommand
{
    /**
     * Execute the command. Check that no link containing the supplied text is present.
     * One parameter is expected, linkText. If more than one link with the same text is expected
     * an optional 0 based parameter can be supplied.
     *
     * @throws Exception is an error occurs.
     */
    public void execute() throws Exception
    {
        if( countParameters() == 2 )
            getWebTester().assertLinkNotPresentWithText( getParameter( 0 ).stringValue(), getParameter( 1 ).intValue() );
        else
            getWebTester().assertLinkNotPresentWithText( getParameter( 0 ).stringValue() );
    }
}
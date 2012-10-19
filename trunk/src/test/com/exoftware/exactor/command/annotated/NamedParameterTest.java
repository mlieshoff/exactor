/******************************************************************
 * Copyright (c) 2012, Exoftware
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
package com.exoftware.exactor.command.annotated;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * This class is a test for class NamedParameter.
 *
 * @author Michael Lieshoff
 */
public class NamedParameterTest extends TestCase {

    private NamedParameter namedNullParameter = new NamedParameter("name", null);
    private NamedParameter namedParameter = new NamedParameter("name", "a,b,c");

    public void testGetSetName() {
        NamedParameter parameter = new NamedParameter("name", "lala");
        assertEquals("name", parameter.getName());
        parameter.setName("abc");
        assertEquals("abc", parameter.getName());
    }

    public void testToString() {
        assertEquals("name=a,b,c", namedParameter.toString());
    }

    public void testToStringWithNull() {
        assertEquals("name=null", namedNullParameter.toString());
    }

    public void testStringValue() {
        assertEquals("a,b,c", namedParameter.stringValue());
    }

    public void testStringValueWithNull() {
        assertNull(namedNullParameter.stringValue());
    }

    public void testSplittedString() {
        List<String> expected = Arrays.asList("a,b,c".split("[,]"));
        assertEquals(expected, Arrays.asList(namedParameter.splittedString("[,]")));
    }

    public void testSplittedStringWithNull() {
        assertNull(namedNullParameter.splittedString("[,]"));
    }

}

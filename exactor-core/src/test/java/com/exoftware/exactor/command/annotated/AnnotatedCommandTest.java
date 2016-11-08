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

import com.exoftware.exactor.Parameter;
import junit.framework.TestCase;

import java.util.Collection;

/**
 * @author Michael Lieshoff
 */
public class AnnotatedCommandTest extends TestCase {

    public void testParameterWasRegistered() {
        FooCommand fooCommand = new FooCommand();
        assertTrue(fooCommand.isParameterRegistered("string"));
    }

    public void testParameterWasNotRegistered() {
        FooCommand fooCommand = new FooCommand();
        assertFalse(fooCommand.isParameterRegistered("bla"));
    }

    public void testHasParameter() {
        FooCommand fooCommand = new FooCommand();
        fooCommand.addParameter(new Parameter("string=hello"));
        assertTrue(fooCommand.hasParameter("string"));
    }

    public void testAddParameter() throws Exception {
        FooCommand fooCommand = new FooCommand();
        fooCommand.addParameter(new Parameter("string=hello"));
        assertEquals("hello", fooCommand.getParameterByName("string").stringValue());
    }

    public void testAddNotNamedParameter() throws Exception {
        FooCommand fooCommand = new FooCommand();
        try {
            fooCommand.addParameter(new Parameter("hello"));
            fail();
        } catch (IllegalArgumentException e) {
            //
        }
    }

    public void testGetParameterMember() throws Exception {
        FooCommand fooCommand = new FooCommand();
        fooCommand.addParameter(new Parameter("string=hello"));
        fooCommand.setUp();
        assertEquals("hello", fooCommand.getMandatoryString());
    }

    public void testGetMandatoryParameterMemberFailsBecauseParameterNotExists() throws Exception {
        FooCommand fooCommand = new FooCommand();
        try {
            fooCommand.setUp();
            fail();
        } catch (IllegalArgumentException e) {
            //
        }
    }

    public void testGetOptionalMissedParameterMember() throws Exception {
        FooCommand fooCommand = new FooCommand();
        fooCommand.addParameter(new Parameter("string=hello"));
        fooCommand.setUp();
        assertEquals("hello", fooCommand.getMandatoryString());
    }

    public void testGetParameterMemberWithUseOfReplacement() throws Exception {
        System.setProperty("foo", "bar");
        FooCommand fooCommand = new FooCommand();
        fooCommand.addParameter(new Parameter("string=[foo]"));
        fooCommand.setUp();
        assertEquals(System.getProperty("foo"), fooCommand.getMandatoryString());
    }

    public void testGetParameters() throws Exception {
        FooCommand fooCommand = new FooCommand();
        fooCommand.addParameter(new Parameter("string=hello"));
        fooCommand.setUp();
        Collection<NamedParameter> actual = fooCommand.getNamedParameters();
        assertEquals(1, actual.size());
        NamedParameter namedParameter = actual.iterator().next();
        assertEquals("string", namedParameter.getName());
        assertEquals("hello", namedParameter.stringValue());

    }

    public void testGetInheritedMember() throws Exception {
        FooInheritCommand fooInheritCommand = new FooInheritCommand();
        fooInheritCommand.addParameter(new Parameter("string=hello"));
        fooInheritCommand.addParameter(new Parameter("integer=4711"));
        fooInheritCommand.setUp();
        assertEquals("hello", fooInheritCommand.getMandatoryString());
        assertEquals(4711, fooInheritCommand.getOptionalInt());
    }

    public void testGetInheritedMemberWithReplace() throws Exception {
        System.setProperty("p0", "hello");
        System.setProperty("p1", "4711");
        FooInheritCommand fooInheritCommand = new FooInheritCommand();
        fooInheritCommand.addParameter(new Parameter("string=[p0]"));
        fooInheritCommand.addParameter(new Parameter("integer=[p1]"));
        fooInheritCommand.setUp();
        assertEquals("hello", fooInheritCommand.getMandatoryString());
        assertEquals(4711, fooInheritCommand.getOptionalInt());
    }

    public void testGetInheritedOptionalMember() throws Exception {
        FooInheritCommand fooInheritCommand = new FooInheritCommand();
        fooInheritCommand.addParameter(new Parameter("string=hello"));
        fooInheritCommand.setUp();
        assertEquals("hello", fooInheritCommand.getMandatoryString());
        assertEquals(1004711, fooInheritCommand.getOptionalInt());
    }

    public void testWrongNameParameterInCommandsParameter() throws Exception {
        try {
            new DefectFooCommand();
            fail();
        } catch (IllegalArgumentException e) {
            //
        }
    }

    public void testParameterWithMaskedEquals() throws Exception {
        FooCommand fooCommand = new FooCommand();
        fooCommand.addParameter(new Parameter("string=bla\\=hello"));
        fooCommand.setUp();
        assertEquals("bla=hello", fooCommand.getMandatoryString());
    }

    public void testParameterWithFunction() throws Exception {
        FooCommand fooCommand = new FooCommand();
        fooCommand.addParameter(new Parameter("string={40+2}"));
        fooCommand.setUp();
        assertEquals("42", fooCommand.getMandatoryString());
    }

}

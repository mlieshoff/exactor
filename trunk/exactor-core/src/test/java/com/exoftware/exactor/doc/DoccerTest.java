/******************************************************************
 * Copyright (c) 2013, Exoftware
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
package com.exoftware.exactor.doc;

import com.exoftware.exactor.command.annotated.FooCommand;
import com.exoftware.exactor.command.annotated.FooNamespace;
import com.exoftware.exactor.command.annotated.ParameterType;
import junit.framework.TestCase;

import java.util.Set;

/**
 * This class defines a test for class Doccer.
 *
 * @author Michael Lieshoff
 */
public class DoccerTest extends TestCase {

    public void testDoc() throws NoSuchFieldException {
        Set<Doccer.Command> result = new Doccer().doc();
        for (Doccer.Command command : result) {
            if (command.name.equals(FooCommand.class.getSimpleName())) {
                assertEquals("The command defines the classical foo.", command.description.text());
                for (Doccer.Meta meta : command.metas) {
                    if ("string".equals(meta.param.name())) {
                        assertEquals("The parameter defines a string.", meta.description.text());
                        assertEquals(FooNamespace.class, meta.param.namespace());
                        assertEquals(ParameterType.MANDATORY, meta.param.type());
                        assertTrue(meta.parameterDefinition.getParameterNames().contains("string"));
                    }
                }
            }
        }
    }

}

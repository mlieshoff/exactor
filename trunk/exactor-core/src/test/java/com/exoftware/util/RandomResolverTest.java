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
package com.exoftware.util;

import junit.framework.TestCase;

/**
 * This class is a test for class RandomResolver.
 *
 * @author Michael Lieshoff
 */
public class RandomResolverTest extends TestCase {

    public void testResolveNull() {
        assertNull(RandomResolver.resolveRandoms(null));
    }

    public void testResolveEmpty() {
        assertEquals("", RandomResolver.resolveRandoms(""));
    }

    public void testResolveNonExistentResolveable() {
        assertEquals("lala", RandomResolver.resolveRandoms("lala"));
    }

    public void testResolveNonResolveable() {
        assertEquals("#lala#", RandomResolver.resolveRandoms("#lala#"));
    }

    public void testResolveWord() {
        String s = RandomResolver.resolveRandoms("#a_word#");
        assertFalse("#a_word#".equals(s));
        assertTrue(s.length() > 0);
    }

    public void testResolveAlphaWord() {
        String s = RandomResolver.resolveRandoms("#an_alphaword#");
        assertFalse("#an_alphaword#".equals(s));
        assertTrue(s.length() > 0);
    }

    public void testResolveString() {
        String s = RandomResolver.resolveRandoms("#a_string#");
        assertFalse("#a_string#".equals(s));
        assertTrue(s.length() > 0);
    }

    public void testGetParametersRandomD100() {
        diceTest(100);
    }

    private void diceTest(int sides) {
        int number = Integer.valueOf(RandomResolver.resolveRandoms("#A_D" + sides + "#"));
        assertTrue(number > 0);
        assertTrue(number <= sides);
    }

    public void testGetParametersRandomD20() {
        diceTest(20);
    }

    public void testGetParametersRandomD12() {
        diceTest(12);
    }

    public void testGetParametersRandomD10() {
        diceTest(10);
    }

    public void testGetParametersRandomD8() {
        diceTest(8);
    }

    public void testGetParametersRandomD6() {
        diceTest(6);
    }

    public void testGetParametersRandomD4() {
        diceTest(4);
    }

    public void testGetParametersRandomByte() {
        byte number = Byte.valueOf(RandomResolver.resolveRandoms("#a_byte#"));
        assertTrue(number >= Byte.MIN_VALUE);
        assertTrue(number <= Byte.MAX_VALUE);
    }

    public void testGetParametersRandomBoolean() {
        boolean bool = Boolean.valueOf(RandomResolver.resolveRandoms("#a_boolean#"));
        assertTrue(bool || !bool);
    }

    public void testGetParametersRandomShort() {
        short number = Short.valueOf(RandomResolver.resolveRandoms("#a_short#"));
        assertTrue(number >= Short.MIN_VALUE);
        assertTrue(number <= Short.MAX_VALUE);
    }

    public void testGetParametersRandomInt() {
        int number = Integer.valueOf(RandomResolver.resolveRandoms("#an_int#"));
        assertTrue(number >= Integer.MIN_VALUE);
        assertTrue(number <= Integer.MAX_VALUE);
    }

    public void testGetParametersRandomFloat() {
        float number = Float.valueOf(RandomResolver.resolveRandoms("#a_float#"));
        assertTrue(number >= Float.MIN_VALUE);
        assertTrue(number <= Float.MAX_VALUE);
    }

    public void testGetParametersRandomLong() {
        long number = Long.valueOf(RandomResolver.resolveRandoms("#a_long#"));
        assertTrue(number >= Long.MIN_VALUE);
        assertTrue(number <= Long.MAX_VALUE);
    }

    public void testGetParametersRandomDouble() {
        double number = Double.valueOf(RandomResolver.resolveRandoms("#a_double#"));
        assertTrue(number >= Double.MIN_VALUE);
        assertTrue(number <= Double.MAX_VALUE);
    }

}

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
 * This class is a test for class RandomUtils.
 *
 * @author Michael Lieshoff
 */
public class RandomUtilsTest extends TestCase {

    public void testAlphaWord() {
        assertIsInBounds(RandomUtils.getAlphaWord(50));
    }

    private void assertIsInBounds(String s) {
        assertNotNull(s);
        assertTrue(s.length() > 0);
        assertTrue(s.length() <= 50);
    }

    public void testWord() {
        assertIsInBounds(RandomUtils.getWord(50));
    }

    public void testString() {
        assertIsInBounds(RandomUtils.getString(50));
    }

    public void testD100() {
        assertDiceIsInRange(100, RandomUtils.d100());
    }

    private void assertDiceIsInRange(int sides, int number) {
        assertTrue(number > 0);
        assertTrue(number <= sides);
    }

    public void testD20() {
        assertDiceIsInRange(20, RandomUtils.d20());
    }

    public void testD12() {
        assertDiceIsInRange(12, RandomUtils.d12());
    }

    public void testD10() {
        assertDiceIsInRange(10, RandomUtils.d10());
    }

    public void testD8() {
        assertDiceIsInRange(8, RandomUtils.d8());
    }

    public void testD6() {
        assertDiceIsInRange(6, RandomUtils.d6());
    }

    public void testD4() {
        assertDiceIsInRange(4, RandomUtils.d4());
    }

    public void testByte() {
        byte number = RandomUtils.getByte();
        assertTrue(number >= Byte.MIN_VALUE);
        assertTrue(number <= Byte.MAX_VALUE);
    }

    public void testBoolean() {
        boolean bool = RandomUtils.getBoolean();
        assertTrue(bool || !bool);
    }

    public void testShort() {
        short number = RandomUtils.getShort();
        assertTrue(number >= Short.MIN_VALUE);
        assertTrue(number <= Short.MAX_VALUE);
    }

    public void testInt() {
        int number = RandomUtils.getInt();
        assertTrue(number >= Integer.MIN_VALUE);
        assertTrue(number <= Integer.MAX_VALUE);
    }

    public void testFloat() {
        float number = RandomUtils.getFloat();
        assertTrue(number >= Float.MIN_VALUE);
        assertTrue(number <= Float.MAX_VALUE);
    }

    public void testLong() {
        long number = RandomUtils.getLong();
        assertTrue(number >= Long.MIN_VALUE);
        assertTrue(number <= Long.MAX_VALUE);
    }

    public void testDouble() {
        double number = RandomUtils.getDouble();
        assertTrue(number >= Double.MIN_VALUE);
        assertTrue(number <= Double.MAX_VALUE);
    }

}

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

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * This class helps with some random stuff.
 *
 * @author Michael Lieshoff
 */
public class RandomUtils {
    private static final Random RANDOM = new Random();

    /**
     * Gets a random word with specified length.
     *
     * @param length length of the word.
     * @return random word with specified length.
     */
    public static String getWord(int length) {
        return RandomStringUtils.randomAlphabetic(getPositiveNumber(length));
    }

    /**
     * Gets a random alpha word with specified length.
     *
     * @param length length of the word.
     * @return random alpha word with specified length.
     */
    public static String getAlphaWord(int length) {
        return RandomStringUtils.randomAlphanumeric(getPositiveNumber(length));
    }

    /**
     * Gets a random string with specified length.
     *
     * @param length length of the string.
     * @return random string with specified length.
     */
    public static String getString(int length) {
        return RandomStringUtils.randomAscii(getPositiveNumber(length));
    }

    /**
     * Gets a random positive number with specified length.
     *
     * @param maximum the maximum of the number.
     * @return random number with specified maximum.
     */
    public static int getPositiveNumber(int maximum) {
        return RANDOM.nextInt(maximum) + 1;
    }

    /**
     * Gets a random boolean.
     *
     * @return random boolean.
     */
    public static boolean getBoolean() {
        return RANDOM.nextInt(1) == 1;
    }

    /**
     * Gets a random byte.
     *
     * @return random byte.
     */
    public static byte getByte() {
        return (byte) RANDOM.nextInt(Byte.MAX_VALUE);
    }

    /**
     * Gets a random short.
     *
     * @return random short.
     */
    public static short getShort() {
        return (short) RANDOM.nextInt(Short.MAX_VALUE);
    }

    /**
     * Gets a random int.
     *
     * @return random int.
     */
    public static int getInt() {
        return RANDOM.nextInt();
    }

    /**
     * Gets a random float.
     *
     * @return random float.
     */
    public static float getFloat() {
        return RANDOM.nextFloat();
    }

    /**
     * Gets a random long.
     *
     * @return random long.
     */
    public static long getLong() {
        return RANDOM.nextLong();
    }

    /**
     * Gets a random double.
     *
     * @return random double.
     */
    public static double getDouble() {
        return RANDOM.nextDouble();
    }

    /**
     * Gets a random dice with 4 sides.
     *
     * @return random dice with 4 sides.
     */
    public static int d4() {
        return getPositiveNumber(4);
    }

    /**
     * Gets a random dice with 6 sides.
     *
     * @return random dice with 6 sides.
     */
    public static int d6() {
        return getPositiveNumber(6);
    }

    /**
     * Gets a random dice with 8 sides.
     *
     * @return random dice with 8 sides.
     */
    public static int d8() {
        return getPositiveNumber(8);
    }

    /**
     * Gets a random dice with 10 sides.
     *
     * @return random dice with 10 sides.
     */
    public static int d10() {
        return getPositiveNumber(10);
    }

    /**
     * Gets a random dice with 12 sides.
     *
     * @return random dice with 12 sides.
     */
    public static int d12() {
        return getPositiveNumber(12);
    }

    /**
     * Gets a random dice with 20 sides.
     *
     * @return random dice with 20 sides.
     */
    public static int d20() {
        return getPositiveNumber(20);
    }

    /**
     * Gets a random dice with 100 sides.
     *
     * @return random dice with 100 sides.
     */
    public static int d100() {
        return getPositiveNumber(100);
    }

}

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

import java.util.HashMap;
import java.util.Map;

/**
 * This class resolves random qualifiers.
 *
 * @author Michael Lieshoff
 */
public class RandomResolver {

    private static Map<String, Function> TAGS = new HashMap<String, Function>() {{
        put("a_boolean", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getBoolean();
            }
        });
        put("a_byte", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getByte();
            }
        });
        put("a_d4", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.d4();
            }
        });
        put("a_d6", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.d6();
            }
        });
        put("a_d8", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.d8();
            }
        });
        put("a_d10", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.d10();
            }
        });
        put("a_d12", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.d12();
            }
        });
        put("a_d20", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.d20();
            }
        });
        put("a_d100", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.d100();
            }
        });
        put("a_double", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getDouble();
            }
        });
        put("a_float", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getFloat();
            }
        });
        put("a_long", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getLong();
            }
        });
        put("a_short", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getShort();
            }
        });
        put("a_string", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getString(50);
            }
        });
        put("a_word", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getWord(50);
            }
        });
        put("an_alphaword", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getAlphaWord(50);
            }
        });
        put("an_int", new Function() {
            @Override
            public Object execute() {
                return RandomUtils.getInt();
            }
        });
    }};

    static interface Function {
        Object execute();
    }

    /**
     * Replaces the qualifiers with random values.
     *
     * @param valueToReplace the value to replace.
     * @return resolves string.
     */
    public static String resolveRandoms(String valueToReplace) {
        if (valueToReplace == null) {
            return null;
        }
        boolean inTag = false;
        StringBuilder result = new StringBuilder();
        StringBuilder tag = new StringBuilder();
        for (int i = 0, n = valueToReplace.length(); i < n; i++) {
            char c = valueToReplace.charAt(i);
            if (c == '#') {
                if (inTag) {
                    if (canResolveTag(tag)) {
                        result.append(resolveTag(tag));
                    } else {
                        result.append("#");
                        result.append(tag);
                        result.append("#");
                    }
                    tag.setLength(0);
                }
                inTag = !inTag;
            } else if (inTag) {
                tag.append(c);
            } else {
                result.append(c);
            }
        }
        if (inTag) {
            result.append("#");
            result.append(tag);
        }
        return result.toString();
    }

    private static String resolveTag(StringBuilder tag) {
        return TAGS.get(tag.toString().toLowerCase()).execute().toString();
    }

    private static boolean canResolveTag(StringBuilder tag) {
        return TAGS.containsKey(tag.toString().toLowerCase());
    }

}

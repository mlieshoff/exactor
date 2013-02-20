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
package com.exoftware.util;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * StringTokenizer with Quoting support.
 * <p/>
 * This class is a copy of the java.util.StringTokenizer API and
 * the behaviour is the same, except that single and doulbe quoted
 * string values are recognized.
 * Delimiters within quotes are not considered delimiters.
 * Quotes can be escaped with '\'.
 *
 * @author Greg Wilkins (gregw)
 * @see java.util.StringTokenizer
 */
public class QuotedStringTokenizer extends StringTokenizer {
    private static final String WHITESPACE = "\t\n\r";

    private String _string;
    private String _delim = WHITESPACE;
    private boolean _returnQuotes = false;
    private boolean _returnTokens = false;
    private StringBuffer _token;
    private boolean _hasToken = false;
    private int _i = 0;
    private int _lastStart = 0;

    public QuotedStringTokenizer(String str, String delim, boolean returnTokens, boolean returnQuotes) {
        super("");
        _string = str;
        if (delim != null) {
            _delim = delim;
        }
        _returnTokens = returnTokens;
        _returnQuotes = returnQuotes;
        if (_delim.indexOf('\'') >= 0 || _delim.indexOf('"') >= 0) {
            throw new Error("Can't use quotes as delimiters: " + _delim);
        }
        _token = new StringBuffer(_string.length() > 1024 ? 512 : _string.length() / 2);
    }

    public QuotedStringTokenizer(String str, String delim, boolean returnTokens) {
        this(str, delim, returnTokens, false);
    }

    public QuotedStringTokenizer(String str, String delim) {
        this(str, delim, false, false);
    }

    public QuotedStringTokenizer(String str) {
        this(str, null, false, false);
    }

    public boolean hasMoreTokens() {
        // Already found a token
        if (_hasToken) {
            return true;
        }
        _lastStart = _i;
        int state = 0;
        boolean escape = false;
        while (_i < _string.length()) {
            char c = _string.charAt(_i++);
            switch (state) {
                case 0: // Start
                    if (_delim.indexOf(c) >= 0) {
                        if (_returnTokens) {
                            _token.append(c);
                            _hasToken = true;
                            return _hasToken;
                        }
                    } else if (c == '\'') {
                        if (_returnQuotes) {
                            _token.append(c);
                        }
                        state = 2;
                    } else if (c == '\"') {
                        if (_returnQuotes) {
                            _token.append(c);
                        }
                        state = 3;
                    } else {
                        _token.append(c);
                        _hasToken = true;
                        state = 1;
                    }
                    continue;
                case 1: // Token
                    _hasToken = true;
                    if (_delim.indexOf(c) >= 0) {
                        if (_returnTokens) {
                            _i--;
                        }
                        return _hasToken;
                    } else if (c == '\'') {
                        if (_returnQuotes) {
                            _token.append(c);
                        }
                        state = 2;
                    } else if (c == '\"') {
                        if (_returnQuotes) {
                            _token.append(c);
                        }
                        state = 3;
                    } else {
                        _token.append(c);
                    }
                    continue;
                case 2: // Single Quote
                    _hasToken = true;
                    if (escape) {
                        escape = false;
                        _token.append(c);
                    } else if (c == '\'') {
                        if (_returnQuotes) {
                            _token.append(c);
                        }
                        state = 1;
                    } else if (c == '\\') {
                        if (_returnQuotes) {
                            _token.append(c);
                        }
                        escape = true;
                    } else {
                        _token.append(c);
                    }
                    continue;
                case 3: // Double Quote
                    _hasToken = true;
                    if (escape) {
                        escape = false;
                        _token.append(c);
                    } else if (c == '\"') {
                        if (_returnQuotes) {
                            _token.append(c);
                        }
                        state = 1;
                    } else if (c == '\\') {
                        if (_returnQuotes) {
                            _token.append(c);
                        }
                        escape = true;
                    } else {
                        _token.append(c);
                    }
                    break;
                default:
            }
        }
        return _hasToken;
    }

    public String nextToken() throws NoSuchElementException {
        if (!hasMoreTokens() || _token == null) {
            throw new NoSuchElementException();
        }
        String t = _token.toString();
        _token.setLength(0);
        _hasToken = false;
        return t;
    }

    public String nextToken(String delim) throws NoSuchElementException {
        _delim = delim;
        _i = _lastStart;
        _token.setLength(0);
        _hasToken = false;
        return nextToken();
    }

    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    public Object nextElement() throws NoSuchElementException {
        return nextToken();
    }

    /**
     * Not implemented.
     */
    public int countTokens() {
        throw new UnsupportedOperationException("The countTokens method is not supported");
    }


    /**
     * Quote a string.
     * The string is quoted only if quoting is required due to
     * embeded delimiters, quote characters or the
     * empty string.
     *
     * @param s The string to quote.
     * @return quoted string
     */
    public static String quote(String s, String delim) {
        if (s == null) {
            return null;
        }
        if (s.length() == 0) {
            return "\"\"";
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\\' || c == '\'' || delim.indexOf(c) >= 0) {
                StringBuffer b = new StringBuffer(s.length() + 8);
                quote(b, s);
                return b.toString();
            }
        }
        return s;
    }

    /**
     * Quote a string into a StringBuffer.
     *
     * @param buf The StringBuffer
     * @param s   The String to quote.
     */
    public static void quote(StringBuffer buf, String s) {
        synchronized (buf) {
            buf.append('"');
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '"') {
                    buf.append("\\\"");
                    continue;
                }
                if (c == '\\') {
                    buf.append("\\\\");
                    continue;
                }
                buf.append(c);
            }
            buf.append('"');
        }
    }

    /**
     * Unquote a string.
     *
     * @param s The string to unquote.
     * @return quoted string
     */
    public static String unquote(String s) {
        if (s == null) {
            return null;
        }
        if (s.length() < 2) {
            return s;
        }
        char first = s.charAt(0);
        char last = s.charAt(s.length() - 1);
        if (first != last || (first != '"' && first != '\'')) {
            return s;
        }
        StringBuffer b = new StringBuffer(s.length() - 2);
        synchronized (b) {
            boolean quote = false;
            for (int i = 1; i < s.length() - 1; i++) {
                char c = s.charAt(i);
                if (c == '\\' && !quote) {
                    quote = true;
                    continue;
                }
                quote = false;
                b.append(c);
            }
            return b.toString();
        }
    }
}
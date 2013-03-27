package com.exoftware.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Convenience class for outputing to a String.
 *
 * @author Brian Swan
 */
public class StringOutputStream extends OutputStream {
    private StringBuffer buffer = new StringBuffer();

    public void write(int b) throws IOException {
        buffer.append((char) b);
    }

    public String toString() {
        return buffer.toString();
    }
}

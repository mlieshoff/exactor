/*
 * ServerAuth.java
 * 
 * 19.02.2014
 * 
 * (c) by Nicando Software GmbH
 */
package com.exoftware.exactor.jenkins;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Andoni del Olmo
 */
public class ServerAuth {

    public static String getEncodedAuth(String username, String password) {
        String auth = username + ":" + password;
        return new String(Base64.encodeBase64(auth.getBytes()));
    }
}
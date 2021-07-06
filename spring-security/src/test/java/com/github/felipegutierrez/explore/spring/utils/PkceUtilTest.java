package com.github.felipegutierrez.explore.spring.utils;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PkceUtilTest {

    PkceUtil pkce = new PkceUtil();
    String regex = "^[a-zA-Z0-9_-]*$";

    @Test
    void generateCodeVerifier() throws UnsupportedEncodingException {
        String codeVerifier = pkce.generateCodeVerifier();
        System.out.println(codeVerifier);

        assertTrue(codeVerifier != null);
        assertEquals(43, codeVerifier.length());
        assertTrue(codeVerifier.matches(regex));
        assertTrue(Base64.isBase64(codeVerifier.getBytes()));
    }

    @Test
    void generateCodeChallenge() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String codeVerifier = pkce.generateCodeVerifier();
        String codeChallenge = pkce.generateCodeChallenge(codeVerifier);

        assertTrue(codeChallenge != null);
        assertEquals(43, codeChallenge.length());
        assertTrue(codeChallenge.matches(regex));
        assertTrue(Base64.isBase64(codeChallenge.getBytes()));
    }

    @Test
    @Disabled("this test is under development")
    void validateCodeChallenge() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String codeVerifier = pkce.generateCodeVerifier();
        String codeChallenge = pkce.generateCodeChallenge(codeVerifier);

        // TODO: validate code challenge on a remote online Keycloak authentication serve.
    }
}
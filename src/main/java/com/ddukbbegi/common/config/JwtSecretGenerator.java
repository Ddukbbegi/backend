package com.ddukbbegi.common.config;

import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class JwtSecretGenerator {
    public static void main(String[] args) {
        byte[] keyBytes = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("jwt.secret.key: " + base64Key);
    }
}
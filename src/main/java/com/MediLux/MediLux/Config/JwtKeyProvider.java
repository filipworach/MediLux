package com.MediLux.MediLux.Config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtKeyProvider {
    public static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
}

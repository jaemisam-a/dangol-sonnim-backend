package com.dangol.dangolsonnimbackend.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration-in-ms}")
    private long accessExpirationInMs;

    public String generateAccessToken(Long userId) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + accessExpirationInMs);
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
package com.dangol.dangolsonnimbackend.config.jwt;

import com.dangol.dangolsonnimbackend.oauth.AuthTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration-in-ms}")
    private long accessExpirationInMs;

    // 액세스 토큰을 생성합니다.
    public String generateAccessToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + accessExpirationInMs);
        return Jwts.builder()
                .setSubject(email)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .setExpiration(expirationDate)
                .compact();
    }

    // 토큰으로 부터 이메일 추출
    public String getEmailFromToken(String token) {
        return this.getTokenClaims(token).getSubject();
    }

    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    // 토큰의 유효성  검사
    public boolean validateToken(String token) {
        return this.getTokenClaims(token) != null;
    }

    @Bean
    public AuthTokenProvider jwtProvider() {
        return new AuthTokenProvider(secretKey);
    }

}

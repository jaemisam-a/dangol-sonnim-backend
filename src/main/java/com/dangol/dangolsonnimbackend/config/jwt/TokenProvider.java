package com.dangol.dangolsonnimbackend.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dangol.dangolsonnimbackend.customer.domain.RefreshToken;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.customer.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class TokenProvider {

    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
     * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";
    private final CustomerRepository customerRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-expiration-in-ms}")
    private long accessExpirationInMs;
    @Value("${jwt.refresh-expiration-in-ms}")
    private long refreshExpirationInMs;
    @Value("${jwt.access-header}")
    private String accessHeader;
    @Value("${jwt.refresh-header}")
    private String refreshHeader;

    // 액세스 토큰을 생성합니다.
    public String generateAccessToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + accessExpirationInMs);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // OAuth2 액세스 토큰을 생성합니다.
    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withClaim(EMAIL_CLAIM, email)
                .withExpiresAt(new Date(now.getTime() + accessExpirationInMs))
                .sign(Algorithm.HMAC512(secret));
    }

    // 리프레시 토큰을 생성합니다.
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshExpirationInMs))
                .sign(Algorithm.HMAC512(secret));
    }

    // AccessToken을 헤더에 실어서 보냅니다.
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 AccessToken : {}" + accessToken);
    }

    // AccessToken + RefreshToken을 헤더에 실어서 보냅니다.
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    // 헤더에서 AccessToken을 추출합니다.
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    // Header에서 RefreshToken을 추출합니다.
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Email 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 이메일 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmail(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secret))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken 검증 -> 유효하지 않다면 예외 발생
                    .getClaim(EMAIL_CLAIM) // claim(Email) 가져오기
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    // AccessToken Header 설정
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    // RefreshToken Header 설정
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    // RefreshToken DB 저장(업데이트)
    public void updateRefreshToken(String email, String generatedRefreshToken) {

        Long customerId = customerRepository.findByEmail(email).get().getId();
        RefreshToken refreshToken = refreshTokenRepository.findById(customerId).orElse(null);

        if (refreshToken == null) {
            refreshToken = new RefreshToken(customerId, generatedRefreshToken);
            refreshTokenRepository.saveAndFlush(refreshToken);
        } else {
            refreshToken.updateRefreshToken(generatedRefreshToken);
            refreshTokenRepository.saveAndFlush(refreshToken);
        }
    }

    // 토큰으로 부터 이메일 추출
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 토큰의 유효성  검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty");
        }
        return false;
    }
}

package com.dangol.dangolsonnimbackend.config.jwt;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.domain.RefreshToken;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.customer.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt 인증 필터
 * "/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 * <p>
 * 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
 * AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청
 * <p>
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR
 * 3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR 방식)
 * 인증 성공 처리는 하지 않고 실패 처리
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final String N0_CHECK_URL = "/sign-in"; // Filter 검사를 하지 않음

    private final TokenProvider tokenProvider;
    private final CustomerRepository customerRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(N0_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 사용자 요청 헤더에서 Refresh Token 추출
        String refreshToken = tokenProvider.extractRefreshToken(request)
                .filter(tokenProvider::validateToken)
                .orElse(null);

        if (refreshToken != null) {
            // Refresh Token 유효성 검사
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    // 리프레시 토큰으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급 메소드
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        refreshTokenRepository.findByRefreshToken(refreshToken)
                .ifPresent(refreshTokenEntity -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(refreshTokenEntity);

                    Long customerId = refreshTokenEntity.getId();
                    Customer customer = customerRepository.getById(customerId);
                    tokenProvider.sendAccessAndRefreshToken(response, tokenProvider.createAccessToken(customer.getEmail()), reIssuedRefreshToken);
                });
    }

    // 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드
    private String reIssueRefreshToken(RefreshToken refreshToken) {
        String reIssuedRefreshToken = tokenProvider.createRefreshToken();
        refreshToken.updateRefreshToken(reIssuedRefreshToken);
        refreshTokenRepository.saveAndFlush(refreshToken);
        return reIssuedRefreshToken;
    }

    // 액세스 토큰으로 유저 정보 찾기 & 인증 메소드
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        tokenProvider.extractAccessToken(request)
                .filter(tokenProvider::validateToken)
                .ifPresent(accessToken -> tokenProvider.extractEmail(accessToken)
                        .ifPresent(email -> customerRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));
        filterChain.doFilter(request, response);
    }

    // 인증 허가 메소드
    public void saveAuthentication(Customer customer) {
        UserDetails userDetailUser = org.springframework.security.core.userdetails.User.builder()
                .username(customer.getEmail())
                .roles(customer.getRoleType().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailUser, null,
                        authoritiesMapper.mapAuthorities(userDetailUser.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}

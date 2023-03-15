package com.dangol.dangolsonnimbackend.oauth2.handler;

import com.dangol.dangolsonnimbackend.config.jwt.TokenProvider;
import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.oauth2.CustomOAuth2User;
import com.dangol.dangolsonnimbackend.oauth2.userInfo.OAuth2UserInfo;
import com.dangol.dangolsonnimbackend.type.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final CustomerRepository customerRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2LoginSuccessHandler.onAuthenticationSuccess() 실행 - OAuth2 로그인 성공");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            OAuth2UserInfo oAuth2UserInfo = (OAuth2UserInfo) authentication.getPrincipal();

            // 손님의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if (oAuth2User.getRoleType().equals(RoleType.GUEST)) {
                String accessToken = tokenProvider.createAccessToken(oAuth2UserInfo.getEmail());
                response.addHeader(tokenProvider.getAccessHeader(), "Bearer " + accessToken);
                response.sendRedirect("/login/profile");

                tokenProvider.sendAccessAndRefreshToken(response, accessToken, null);
                Customer findCustomer = customerRepository.findByEmail(oAuth2UserInfo.getEmail())
                        .orElseThrow(() -> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND));
                findCustomer.authorizeCustomer();

            } else {
                loginSuccess(response, oAuth2UserInfo);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void loginSuccess(HttpServletResponse response, OAuth2UserInfo oAuth2UserInfo) throws IOException {
        String accessToken = tokenProvider.createAccessToken(oAuth2UserInfo.getEmail());
        String refreshToken = tokenProvider.createRefreshToken();
        response.addHeader(tokenProvider.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(tokenProvider.getRefreshHeader(), "Bearer" + refreshToken);

        tokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        tokenProvider.updateRefreshToken(oAuth2UserInfo.getEmail(), tokenProvider.createRefreshToken());
    }
}

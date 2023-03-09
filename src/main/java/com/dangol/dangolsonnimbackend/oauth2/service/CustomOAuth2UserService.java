package com.dangol.dangolsonnimbackend.oauth2.service;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.oauth2.CustomOAuth2User;
import com.dangol.dangolsonnimbackend.oauth2.dto.OAuthAttributes;
import com.dangol.dangolsonnimbackend.type.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    private final CustomerRepository customerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // socialType에 맞는 OAuthAttributes 객체 생성
        OAuthAttributes extractedAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        Customer createdCustomer = getCustomer(socialType, extractedAttributes);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdCustomer.getRoleType().getCode())),
                attributes,
                extractedAttributes.getNameAttributeKey(),
                createdCustomer.getRoleType()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if (registrationId.equals(NAVER)) {
            return SocialType.NAVER;
        }
        if (registrationId.equals(KAKAO)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }

    private Customer getCustomer(SocialType socialType, OAuthAttributes attributes) {
        Customer findCustomer = customerRepository.findBySocialTypeAndSocialId(socialType,
                attributes.getOauth2UserInfo().getId()).orElse(null);

        if (findCustomer == null) {
            return saveCustomer(socialType, attributes);
        }
        return findCustomer;
    }

    private Customer saveCustomer(SocialType socialType, OAuthAttributes attributes) {
        Customer createdCustomer = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        return customerRepository.save(createdCustomer);
    }
}

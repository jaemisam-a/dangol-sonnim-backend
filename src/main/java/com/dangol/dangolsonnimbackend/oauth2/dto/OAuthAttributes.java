package com.dangol.dangolsonnimbackend.oauth2.dto;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.dangol.dangolsonnimbackend.oauth2.userInfo.KakaoOAuth2UserInfo;
import com.dangol.dangolsonnimbackend.oauth2.userInfo.NaverOAuth2UserInfo;
import com.dangol.dangolsonnimbackend.oauth2.userInfo.OAuth2UserInfo;
import com.dangol.dangolsonnimbackend.type.RoleType;
import com.dangol.dangolsonnimbackend.type.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * 소셜별로 받아오는 데이터가 다르므로 각각 분기 처리하는 DTO 클래스
 */
@Getter
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes) {

        if (socialType == SocialType.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        } else if (socialType == SocialType.KAKAO) {
            return ofKaKao(userNameAttributeName, attributes);
        } else if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        } else {
            throw new IllegalArgumentException("해당 소셜 타입은 지원하지 않습니다.");
        }
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofKaKao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public Customer toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return Customer.builder()
                .name("")
                .nickname("")
                .email(oauth2UserInfo.getEmail())
                .profileImageUrl(oauth2UserInfo.getImageUrl())
                .phoneNumber("")
                .birth("")
                .marketingAgreement(true)
                .roleType(RoleType.GUEST)
                .socialId(oauth2UserInfo.getId())
                .socialType(socialType)
                .build();
    }

}

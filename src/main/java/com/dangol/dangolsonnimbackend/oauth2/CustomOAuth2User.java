package com.dangol.dangolsonnimbackend.oauth2;

import com.dangol.dangolsonnimbackend.type.RoleType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * DefaultOAuth2User를 상속받아 사용자 정보를 추가로 저장
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private RoleType roleType;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      사용자에게 부여된 권한
     * @param attributes       사용자에 대한 속성
     * @param nameAttributeKey 사용자의 "이름"에 해당하는 키
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            RoleType roleType) {
        super(authorities, attributes, nameAttributeKey);
        this.roleType = roleType;
    }
}

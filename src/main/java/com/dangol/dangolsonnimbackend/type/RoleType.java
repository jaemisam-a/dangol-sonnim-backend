package com.dangol.dangolsonnimbackend.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    GUEST("ROLE_GUEST", "소셜 로그인만 인증 단계"),
    USER("ROLE_USER", "회원가입 완료");

    private final String code;
    private final String description;
}

package com.dangol.dangolsonnimbackend.errors.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeMessage {
    ALREADY_EXISTS_STORE_REGISTER_NUMBER("이미 존재하는 사업자 등록 번호입니다."),
    ALREADY_EXISTS_EMAIL("이미 존재하는 이메일입니다."),
    ALREADY_EXISTS_PHONE_NUMBER("이미 존재하는 휴대폰 번호입니다."),
    PASSWORD_NOT_MATCH("패스워드가 일치하지 않습니다."),
    BOSS_NOT_FOUND("존재하지 않는 사장님입니다."),
    SUBSCRIBE_NOT_FOUND("존재하지 않는 구독권입니다."),
    INVALID_SUBSCRIBE_TYPE("유효하지 않은 구독권 타입입니다.");

    private String message;
}
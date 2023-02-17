package com.dangol.dangolsonnimbackend.errors.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeMessage {
    ALREADY_EXISTS_STORE_REGISTER_NUMBER("이미 존재하는 사업자 등록 번호입니다."),
    PASSWORD_NOT_MATCH("해당 비밀번호가 일치하지 않습니다."),
    BOSS_NOT_FOUND("해당 이메일을 가진 사장님이 존재하지 않습니다.");

    private String message;
}
package com.dangol.dangolsonnimbackend.errors.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeMessage {
    ALREADY_EXISTS_STORE_REGISTER_NUMBER("이미 존재하는 사업자 등록 번호입니다."),
    BOSS_NOT_FOUND("존재하지 않는 사장님입니다.");

    private String message;
}
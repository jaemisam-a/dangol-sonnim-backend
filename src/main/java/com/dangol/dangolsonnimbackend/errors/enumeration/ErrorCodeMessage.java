package com.dangol.dangolsonnimbackend.errors.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeMessage {
    ALREADY_EXISTS_STORE_REGISTER_NUMBER("이미 존재하는 사업자 등록 번호입니다.");

    private String message;
}
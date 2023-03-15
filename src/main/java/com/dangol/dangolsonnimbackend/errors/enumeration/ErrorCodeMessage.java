package com.dangol.dangolsonnimbackend.errors.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeMessage {
    // TODO. 상태코드 별로 에러 메세지 분류

    ALREADY_EXISTS_STORE_REGISTER_NUMBER("이미 존재하는 사업자 등록 번호입니다."),
    ALREADY_EXISTS_EMAIL("이미 존재하는 이메일입니다."),
    ALREADY_EXISTS_PHONE_NUMBER("이미 존재하는 휴대폰 번호입니다."),
    ALREADY_EXISTS_NICKNAME("이미 존재하는 닉네임입니다."),
    PASSWORD_NOT_MATCH("패스워드가 일치하지 않습니다."),
    BOSS_NOT_FOUND("존재하지 않는 사장님입니다."),
    CUSTOMER_NOT_FOUND("존재하지 않는 손님입니다."),
    SUBSCRIBE_NOT_FOUND("존재하지 않는 구독권입니다."),
    INVALID_SUBSCRIBE_TYPE("유효하지 않은 구독권 타입입니다."),
    STORE_NOT_FOUND("존재하지 않는 가게입니다."),
    DAILY_EMAIL_RESTRICTED("일일 전송 허용 횟수를 초과하였습니다."),
    REQUEST_NOT_INVALID("요청 데이터의 JSON 형식이 적절하지 않습니다."),

    // 500
    RESPONSE_CREATE_ERROR("응답 데이터를 생성하던 도중 에러가 발생하였습니다."),
    EMAIL_SEND_FAILURE("이메일 전송에 실패하였습니다.");

    private final String message;
}

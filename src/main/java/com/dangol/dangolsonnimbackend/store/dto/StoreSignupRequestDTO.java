package com.dangol.dangolsonnimbackend.store.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StoreSignupRequestDTO {
    @NotNull(message = "이름은 Null 일 수 없습니다.")
    private String name;

    @NotNull(message = "휴대폰 번호는 Null 일 수 없습니다.")
    private String storePhoneNumber;

    @NotNull(message = "도로명 주소는 Null 일 수 없습니다.")
    private String newAddress;

    @NotNull(message = "시/도는 Null 일 수 없습니다.")
    private String sido;

    @NotNull(message = "시/군/구는 Null 일 수 없습니다.")
    private String sigungu;

    private String bname1;

    private String bname2;

    @NotNull(message = "상세주소는 Null 일 수 없습니다.")
    private String detailedAddress;

    @NotNull(message = "한줄소개는 Null 일 수 없습니다.")
    private String comments;

    @NotNull(message = "영업시간은 Null 일 수 없습니다.")
    private String officeHours;
}

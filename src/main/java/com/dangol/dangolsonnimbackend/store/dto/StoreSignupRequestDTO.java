package com.dangol.dangolsonnimbackend.store.dto;

import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreSignupRequestDTO {
    @NotNull(message = "가게이름은 Null 일 수 없습니다.")
    private String name;

    @NotNull(message = "휴대폰 번호는 Null 일 수 없습니다.")
    private String phoneNumber;

    @NotNull(message = "도로명 주소는 Null 일 수 없습니다.")
    private String newAddress;

    @NotNull(message = "시/도는 Null 일 수 없습니다.")
    private String sido;

    @NotNull(message = "시/군/구는 Null 일 수 없습니다.")
    private String sigungu;

    private String bname1;

    private String bname2;

    private String detailedAddress;

    @NotNull(message = "한줄소개는 Null 일 수 없습니다.")
    private String comments;

    @NotNull(message = "카테고리는 Null 일 수 없습니다.")
    private CategoryType categoryType;

    @NotNull(message = "사업자번호는 Null 일 수 없습니다.")
    private String registerNumber;

    @NotNull(message = "사업자명은 Null 일 수 없습니다.")
    private String registerName;

    @NotNull(message = "태그는 Null 일 수 없습니다.")
    private List<String> tags;

    @NotNull(message = "영업시간은 Null 일 수 없습니다.")
    private List<BusinessHourRequestDTO> businessHours;
}

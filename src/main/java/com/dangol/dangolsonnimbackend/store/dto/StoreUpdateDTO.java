package com.dangol.dangolsonnimbackend.store.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreUpdateDTO {
    private String name;

    private String phoneNumber;

    private String newAddress;

    private String sido;

    private String sigungu;

    private String bname1;

    private String bname2;

    private String detailedAddress;

    private String comments;

    private String officeHours;

    private Long categoryId;

    @NotNull
    private String registerNumber;

    private String registerName;
}

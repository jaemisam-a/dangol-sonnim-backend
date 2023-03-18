package com.dangol.dangolsonnimbackend.store.dto;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponseDTO {
    private Long id;

    private String name;

    private String newAddress;

    private String comments;

    private String sido;

    private String sigungu;

    private String bname1;

    private String bname2;

    private String detailedAddress;

    private String registerNumber;

    private String registerName;

    // TODO. 태그 및 이미지 필드 추가

    public StoreResponseDTO(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.newAddress = store.getNewAddress();
        this.comments = store.getComments();
        this.sido = store.getSido();
        this.sigungu = store.getSigungu();
        this.bname1 = store.getBname1();
        this.bname2 = store.getBname2();
        this.detailedAddress = store.getDetailedAddress();
        this.registerNumber = store.getRegisterNumber();
        this.registerName = store.getRegisterName();
    }
}

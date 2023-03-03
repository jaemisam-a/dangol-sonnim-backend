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

    private Long categoryId;

    private String comments;

    private String sigungu;

    private String bname1;

    private String bname2;

    private String detailedAddress;

    // TODO. 태그 및 이미지 필드 추가

    public StoreResponseDTO(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.newAddress = store.getNewAddress();
        this.categoryId = store.getCategoryId();
        this.comments = store.getComments();
        this.sigungu = store.getSigungu();
        this.bname1 = store.getBname1();
        this.bname2 = store.getBname2();
        this.detailedAddress = store.getDetailedAddress();
    }
}

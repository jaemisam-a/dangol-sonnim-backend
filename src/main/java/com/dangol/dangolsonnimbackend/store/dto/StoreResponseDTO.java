package com.dangol.dangolsonnimbackend.store.dto;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.domain.StoreImage;
import com.dangol.dangolsonnimbackend.store.domain.Tag;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

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
    private Integer likeNumber;

    private CategoryType categoryType;
    private List<String> tags;
    private List<BusinessHourRequestDTO> businessHours;
    private List<String> storeImageUrlList;

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
        this.categoryType = store.getCategory().getCategoryType();
        this.tags = store.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        this.businessHours = store.getBusinessHours().stream().map(BusinessHourRequestDTO::new)
                .collect(Collectors.toList());
        this.storeImageUrlList = store.getStoreImages().stream().map(StoreImage::getImageUrl)
                .collect(Collectors.toList());
        this.likeNumber = store.getLikeNumber();
    }
}

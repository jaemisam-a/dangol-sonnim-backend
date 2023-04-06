package com.dangol.dangolsonnimbackend.store.dto;

import com.dangol.dangolsonnimbackend.errors.InternalServerException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.domain.StoreImage;
import com.dangol.dangolsonnimbackend.store.domain.Tag;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
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
public class StoreDetailResponseDTO {
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

    private CategoryType categoryType;
    private List<String> tags;
    private List<BusinessHourRequestDTO> businessHours;
    private List<MenuResponseDTO> menuResponseDTOList;
    private List<SubscribeResponseDTO> subscribeResponseDTOList;
    private List<String> storeImageUrlList;

    public StoreDetailResponseDTO(Store store) {
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
        this.menuResponseDTOList = store.getMenuList().stream().map(MenuResponseDTO::new)
                .collect(Collectors.toList());
        this.subscribeResponseDTOList = store.getSubscribeList().stream().map(subscribe -> {
            if (subscribe instanceof CountSubscribe) {
                return new SubscribeResponseDTO((CountSubscribe) subscribe);
            } else if (subscribe instanceof MonthlySubscribe) {
                return new SubscribeResponseDTO((MonthlySubscribe) subscribe);
            } else {
                throw new InternalServerException(ErrorCodeMessage.INVALID_SUBSCRIBE_TYPE);
            }
        }).collect(Collectors.toList());
        this.storeImageUrlList = store.getStoreImages().stream().map(StoreImage::getImageUrl)
                .collect(Collectors.toList());
    }
}
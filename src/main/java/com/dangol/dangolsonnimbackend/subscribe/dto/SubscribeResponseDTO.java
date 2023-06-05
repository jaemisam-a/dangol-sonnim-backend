package com.dangol.dangolsonnimbackend.subscribe.dto;

import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeResponseDTO {
    private SubscribeType type;
    private Long subscribeId;
    private Long storeId;
    private String name;
    private BigDecimal price;
    private String intro;
    private Boolean isTop;
    private Integer useCount;
    private String createAt;
    private String modifiedAt;
    private List<BenefitDTO> benefits;
    private String storeName;

    public SubscribeResponseDTO(CountSubscribe subscribe){
        this.type = SubscribeType.COUNT;
        this.subscribeId = subscribe.getId();
        this.storeId = subscribe.getStore().getId();
        this.price = subscribe.getPrice();
        this.name = subscribe.getName();
        this.intro = subscribe.getIntro();
        this.isTop = subscribe.getIsTop();
        this.useCount = subscribe.getUseCount();
        this.createAt = String.valueOf(subscribe.getCreatedAt());
        this.modifiedAt = String.valueOf(subscribe.getModifiedAt());
        this.benefits = subscribe.getBenefits().stream().map(BenefitDTO::new).collect(Collectors.toList());
        this.storeName = subscribe.getStore().getName();
    }

    public SubscribeResponseDTO(MonthlySubscribe subscribe){
        this.type = SubscribeType.MONTHLY;
        this.subscribeId = subscribe.getId();
        this.storeId = subscribe.getStore().getId();
        this.price = subscribe.getPrice();
        this.name = subscribe.getName();
        this.intro = subscribe.getIntro();
        this.isTop = subscribe.getIsTop();
        this.createAt = String.valueOf(subscribe.getCreatedAt());
        this.modifiedAt = String.valueOf(subscribe.getModifiedAt());
        this.benefits = subscribe.getBenefits().stream().map(BenefitDTO::new).collect(Collectors.toList());
        this.storeName = subscribe.getStore().getName();
    }
}

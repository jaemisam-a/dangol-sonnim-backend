package com.dangol.dangolsonnimbackend.subscribe.dto;

import com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedSubscribeResponseDTO {

    private Long purchasedSubscribeId;

    private String merchantUid;
    private SubscribeType subscribeType;

    private String storeTitle;

    private String sigungu;
    private String bname1;
    private String bname2;

    private String subscribeName;
    private List<BenefitDTO> benefits;

    private String QRImageUrl;

    private Integer totalCount;
    private Integer remainingCount;
    private String createdAt;
    private String expiredAt;

    private String intro;
    private BigDecimal price;
}

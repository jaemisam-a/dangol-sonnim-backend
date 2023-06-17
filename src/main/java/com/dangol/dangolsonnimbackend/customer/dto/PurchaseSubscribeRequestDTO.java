package com.dangol.dangolsonnimbackend.customer.dto;

import com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseSubscribeRequestDTO {

    @NotNull(message = "merchantUid 는 Null 일 수 없습니다.")
    private String merchantUid;

    @NotNull(message = "구독권 ID는 Null 일 수 없습니다.")
    private Long subscribeId;

    @NotNull(message = "구독권 타입은 Null 일 수 없습니다.")
    private SubscribeType subscribeType;
}

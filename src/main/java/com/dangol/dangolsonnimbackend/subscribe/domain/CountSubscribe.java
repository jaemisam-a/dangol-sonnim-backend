package com.dangol.dangolsonnimbackend.subscribe.domain;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("COUNT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CountSubscribe extends Subscribe{
    private Integer useCount;

    @Builder
    public CountSubscribe(SubscribeRequestDTO dto, Store store) {
        super(dto.getName(), dto.getPrice(), dto.getIntro(), dto.getIsTop(), store);
        this.useCount = dto.getUseCount();
    }
}

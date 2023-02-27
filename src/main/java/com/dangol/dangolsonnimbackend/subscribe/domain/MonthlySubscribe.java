package com.dangol.dangolsonnimbackend.subscribe.domain;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Getter
@DiscriminatorValue("MONTHLY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlySubscribe extends Subscribe{

    @Builder
    public MonthlySubscribe(SubscribeRequestDTO dto, Store store) {
        super(dto.getName(), dto.getPrice(), dto.getIntro(), dto.getIsTop(), store);
    }
}

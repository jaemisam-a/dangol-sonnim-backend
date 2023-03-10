package com.dangol.dangolsonnimbackend.subscribe.domain;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("MONTHLY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlySubscribe extends Subscribe{

    @Builder
    public MonthlySubscribe(SubscribeRequestDTO dto, Store store) {
        super(dto.getName(), dto.getPrice(), dto.getIntro(), dto.getIsTop(), store);
    }

    @Override
    public SubscribeResponseDTO toResponseDTO() {
        return new SubscribeResponseDTO(this);
    }
}

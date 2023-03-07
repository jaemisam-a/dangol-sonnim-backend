package com.dangol.dangolsonnimbackend.subscribe.domain;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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

    @Override
    public SubscribeResponseDTO toResponseDTO() {
        return new SubscribeResponseDTO(this);
    }
}

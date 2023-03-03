package com.dangol.dangolsonnimbackend.subscribe.service.impl;

import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
import com.dangol.dangolsonnimbackend.subscribe.repository.SubscribeRepository;
import com.dangol.dangolsonnimbackend.subscribe.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscribeRepository<Subscribe> subscribeRepository;
    // 가게 관련 서비스 머지 시 QueryDsl Repository로 수정할 예정
    private final StoreRepository storeRepository;

    @Autowired
    public SubscribeServiceImpl(SubscribeRepository<Subscribe> subscribeRepository, StoreRepository storeRepository){
        this.subscribeRepository = subscribeRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional
    public SubscribeResponseDTO create(SubscribeRequestDTO dto) {
        // 가게 관련 머지 시 수정 예정
        Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(
                () -> new BadRequestException(ErrorCodeMessage.BOSS_NOT_FOUND)
        );
        Subscribe savedSubscribe = subscribeRepository.save(classify(dto, store));
        return savedSubscribe.toResponseDTO();
    }

    public Subscribe classify(SubscribeRequestDTO dto, Store store){
        switch (dto.getType()) {
            case MONTHLY:
                return new MonthlySubscribe(dto, store);
            case COUNT:
                return new CountSubscribe(dto, store);
            default:
                throw new BadRequestException(ErrorCodeMessage.INVALID_SUBSCRIBE_TYPE);
        }
    }
}

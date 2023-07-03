package com.dangol.dangolsonnimbackend.subscribe.service;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import com.dangol.dangolsonnimbackend.subscribe.dto.PurchasedSubscribeResponseDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;

import java.util.List;

public interface SubscribeService {
    SubscribeResponseDTO create(SubscribeRequestDTO dto);
    Subscribe classify(SubscribeRequestDTO dto, Store store);

    SubscribeResponseDTO getSubscribe(Long subscribeId);

    void deleteSubscribe(Long subscribeId);

    List<PurchasedSubscribeResponseDTO> getSubscribeList(String id);

    PurchasedSubscribeResponseDTO useSubscribe(Long subscribeId);
}

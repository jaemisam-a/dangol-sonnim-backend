package com.dangol.dangolsonnimbackend.subscribe.service;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;

public interface SubscribeService {
    SubscribeResponseDTO create(SubscribeRequestDTO dto);
    Subscribe classify(SubscribeRequestDTO dto, Store store);
}

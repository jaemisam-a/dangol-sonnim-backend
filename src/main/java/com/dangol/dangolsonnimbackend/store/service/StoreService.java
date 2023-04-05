package com.dangol.dangolsonnimbackend.store.service;

import com.dangol.dangolsonnimbackend.store.dto.StoreResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;

public interface StoreService {
    StoreResponseDTO create(StoreSignupRequestDTO dto);
    StoreResponseDTO findById(Long id);
    StoreResponseDTO updateStoreByDto(StoreUpdateDTO dto);
}

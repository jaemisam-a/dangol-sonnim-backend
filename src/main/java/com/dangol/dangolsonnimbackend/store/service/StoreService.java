package com.dangol.dangolsonnimbackend.store.service;

import com.dangol.dangolsonnimbackend.store.dto.StoreResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;

import java.util.List;

public interface StoreService {
    StoreResponseDTO create(StoreSignupRequestDTO dto, String email);
    StoreResponseDTO findById(Long id);
    StoreResponseDTO updateStoreByDto(StoreUpdateDTO dto);

    List<StoreResponseDTO> findMyStore(String email);
}

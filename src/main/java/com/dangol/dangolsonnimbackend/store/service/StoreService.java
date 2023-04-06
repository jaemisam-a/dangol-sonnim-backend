package com.dangol.dangolsonnimbackend.store.service;

import com.dangol.dangolsonnimbackend.store.dto.*;

import java.util.List;

public interface StoreService {
    StoreResponseDTO create(StoreSignupRequestDTO dto, String email);
    StoreResponseDTO findById(Long id);
    StoreResponseDTO updateStoreByDto(StoreUpdateDTO dto);

    List<StoreResponseDTO> findMyStore(String email);

    void imageUpload(StoreImageUploadRequestDTO dto);
}

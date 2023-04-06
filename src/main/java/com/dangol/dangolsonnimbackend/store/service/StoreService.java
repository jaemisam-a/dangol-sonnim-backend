package com.dangol.dangolsonnimbackend.store.service;

import com.dangol.dangolsonnimbackend.store.dto.*;

import java.util.List;

public interface StoreService {
    StoreResponseDTO create(StoreSignupRequestDTO dto, String email);
    StoreDetailResponseDTO findById(Long id);
    StoreResponseDTO updateStoreByDto(StoreUpdateDTO dto);

    List<StoreResponseDTO> findMyStore(String email);

    void imageUpload(StoreImageUploadRequestDTO dto);
}

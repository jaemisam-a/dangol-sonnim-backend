package com.dangol.dangolsonnimbackend.store.service;

import com.dangol.dangolsonnimbackend.store.dto.*;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    StoreResponseDTO create(StoreSignupRequestDTO dto, String email);
    StoreDetailResponseDTO findById(Long id);
    StoreResponseDTO updateStoreByDto(StoreUpdateDTO dto, Long storeId);

    List<StoreResponseDTO> findMyStore(String email);

    void imageUpload(StoreImageUploadRequestDTO dto);
    Page<StoreResponseDTO> findStoreList(String sigungu, CategoryType category, String kw, Pageable pageable);
}

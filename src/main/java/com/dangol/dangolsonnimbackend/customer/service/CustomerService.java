package com.dangol.dangolsonnimbackend.customer.service;


import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.IsValidAccessTokenRequestDTO;
import com.dangol.dangolsonnimbackend.customer.dto.*;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface CustomerService {

    CustomerResponseDTO addInfo(String id, CustomerInfoRequestDTO dto);

    CustomerResponseDTO getInfo(String id);

    void existsByNickname(String nickname);

    void like(String id, Long storeId);

    Boolean isLike(String id, Long storeId);

    void withdraw(String id);

    CustomerResponseDTO update(String id, CustomerUpdateRequestDTO reqeustDTO);

    void accessTokenValidate(IsValidAccessTokenRequestDTO dto);

    void purchaseSubscribe(String id, PurchaseSubscribeRequestDTO dto) throws IOException, WriterException;
}

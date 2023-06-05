package com.dangol.dangolsonnimbackend.customer.service;


import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossResponseDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerInfoRequestDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerResponseDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupRequestDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerUpdateRequestDTO;

public interface CustomerService {

    CustomerResponseDTO addInfo(String id, CustomerInfoRequestDTO dto);

    CustomerResponseDTO getInfo(String id);

    void existsByNickname(String nickname);

    void like(String id, Long storeId);

    Boolean isLike(String id, Long storeId);

    void withdraw(String id);

    CustomerResponseDTO update(String id, CustomerUpdateRequestDTO reqeustDTO);
}

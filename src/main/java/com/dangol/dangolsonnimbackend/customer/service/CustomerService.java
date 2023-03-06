package com.dangol.dangolsonnimbackend.customer.service;


import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupRequestDTO;

public interface CustomerService {
    void signup(CustomerSignupRequestDTO dto);
}

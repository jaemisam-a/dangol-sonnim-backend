package com.dangol.dangolsonnimbackend.customer.service.impl;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.domain.CustomerInfo;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupRequestDTO;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerInfoRepository;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.customer.repository.dsl.CustomerInfoQueryRepository;
import com.dangol.dangolsonnimbackend.customer.service.CustomerService;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerInfoRepository customerInfoRepository;
    private final CustomerInfoQueryRepository customerInfoQueryRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerInfoRepository customerInfoRepository, CustomerInfoQueryRepository customerInfoQueryRepository) {
        this.customerRepository = customerRepository;
        this.customerInfoRepository = customerInfoRepository;
        this.customerInfoQueryRepository = customerInfoQueryRepository;
    }

    @Transactional
    public void signup(CustomerSignupRequestDTO dto) {

        validateDuplicateCustomerInfo(dto);

        customerRepository.save(new Customer(dto));
        customerInfoRepository.save(new CustomerInfo(dto));
    }

    private void validateDuplicateCustomerInfo(CustomerSignupRequestDTO dto) {
        if (customerInfoQueryRepository.existsByNickname(dto.getNickname())) {
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_NICKNAME);
        }
        if (customerInfoQueryRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_PHONE_NUMBER);
        }
    }
}

package com.dangol.dangolsonnimbackend.customer.service.impl;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupDTO;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.customer.repository.dsl.CustomerQueryRepository;
import com.dangol.dangolsonnimbackend.customer.service.CustomerService;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerQueryRepository customerQueryRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerQueryRepository customerQueryRepository) {
        this.customerRepository = customerRepository;
        this.customerQueryRepository = customerQueryRepository;
    }

    @Transactional
    public void signup(CustomerSignupDTO dto) {

        validateDuplicateCustomerInfo(dto);

        customerRepository.save(new Customer(dto));
    }

    private void validateDuplicateCustomerInfo(CustomerSignupDTO dto) {
        if (customerQueryRepository.existsByNickname(dto.getNickname())) {
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_NICKNAME);
        }
        if (customerQueryRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_PHONE_NUMBER);
        }
    }
}

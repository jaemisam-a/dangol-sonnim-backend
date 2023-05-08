package com.dangol.dangolsonnimbackend.customer.service.impl;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerInfoRequestDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerResponseDTO;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerInfoRepository;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.customer.service.CustomerService;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.file.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerInfoRepository customerInfoRepository;
    private final FileService fileService;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerInfoRepository customerInfoRepository, FileService fileService) {
        this.customerRepository = customerRepository;
        this.customerInfoRepository = customerInfoRepository;
        this.fileService = fileService;
    }

    @Override
    public CustomerResponseDTO addInfo(String id, CustomerInfoRequestDTO dto) {
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND)
        );
        String s3FileUrl = uploadFileIfPresent(dto.getMultipartFile());
        customer.getCustomerInfo().setCustomerInfo(dto, s3FileUrl);
        return new CustomerResponseDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getInfo(String id){
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND)
        );
        return new CustomerResponseDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public void existsByNickname(String nickname) {
        if(customerInfoRepository.existsByNickname(nickname)){
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_NICKNAME);
        }
    }

    private String uploadFileIfPresent(MultipartFile file) {
        if (file != null) {
            return fileService.upload(file);
        }
        return "";
    }
}

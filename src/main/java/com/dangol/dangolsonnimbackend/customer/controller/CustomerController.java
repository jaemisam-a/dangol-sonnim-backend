package com.dangol.dangolsonnimbackend.customer.controller;

import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupDTO;
import com.dangol.dangolsonnimbackend.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody CustomerSignupDTO dto) {
        customerService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

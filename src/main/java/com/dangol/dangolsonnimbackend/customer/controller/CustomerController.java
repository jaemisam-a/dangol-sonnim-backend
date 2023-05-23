package com.dangol.dangolsonnimbackend.customer.controller;

import com.dangol.dangolsonnimbackend.customer.dto.CustomerInfoRequestDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerResponseDTO;
import com.dangol.dangolsonnimbackend.customer.dto.IsLikeResponseDTO;
import com.dangol.dangolsonnimbackend.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/info")
    public ResponseEntity<CustomerResponseDTO> addInfo(@AuthenticationPrincipal String id, @Valid @ModelAttribute CustomerInfoRequestDTO dto){
        CustomerResponseDTO responseDTO = customerService.addInfo(id, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<CustomerResponseDTO> getInfo(@AuthenticationPrincipal String id){
        CustomerResponseDTO responseDTO = customerService.getInfo(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/validate/{nickname}")
    public ResponseEntity<Void> isNicknameDuplicate(@Valid @PathVariable String nickname){
        customerService.existsByNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/like/{storeId}")
    public ResponseEntity<Void> like(@AuthenticationPrincipal String id, @PathVariable Long storeId){
        customerService.like(id, storeId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/isLike/{storeId}")
    public ResponseEntity<IsLikeResponseDTO> isLike(@AuthenticationPrincipal String id, @PathVariable Long storeId){
        if (id == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new IsLikeResponseDTO(Boolean.FALSE));
        }
        Boolean isLike = customerService.isLike(id, storeId);
        return ResponseEntity.status(HttpStatus.OK).body(new IsLikeResponseDTO(isLike));
    }
}

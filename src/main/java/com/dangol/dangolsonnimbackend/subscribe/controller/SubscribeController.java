package com.dangol.dangolsonnimbackend.subscribe.controller;

import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
import com.dangol.dangolsonnimbackend.subscribe.service.SubscribeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/subscribe")
public class SubscribeController {


    private final SubscribeService subscribeService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping
    public ResponseEntity<SubscribeResponseDTO> create(@Valid @RequestBody SubscribeRequestDTO dto) {
        SubscribeResponseDTO responseDTO = subscribeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{subscribeId}")
    public ResponseEntity<SubscribeResponseDTO> getSubscribe(@PathVariable Long subscribeId){
        SubscribeResponseDTO responseDTO = subscribeService.getSubscribe(subscribeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}

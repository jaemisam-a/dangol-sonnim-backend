package com.dangol.dangolsonnimbackend.store.controller;

import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.store.dto.*;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;
    private final ObjectMapper objectMapper;

    @Autowired
    public StoreController(StoreService storeService, ObjectMapper objectMapper) {
        this.storeService = storeService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<StoreResponseDTO> create(@Valid @RequestBody StoreSignupRequestDTO dto,
                                                   @AuthenticationPrincipal String email) {
        StoreResponseDTO res = storeService.create(dto, email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @GetMapping("/find/{storeId}")
    public ResponseEntity<StoreDetailResponseDTO> findById(@PathVariable Long storeId) {
        StoreDetailResponseDTO res = storeService.findById(storeId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @GetMapping("/my-store")
    public ResponseEntity<List<StoreResponseDTO>> findMyStore(@AuthenticationPrincipal String email) {
        List<StoreResponseDTO> res = storeService.findMyStore(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @PatchMapping("/update")
    public ResponseEntity<StoreResponseDTO> update(@RequestBody String jsonString) throws BadRequestException {
        try {
            StoreUpdateDTO dto = objectMapper.readValue(jsonString, StoreUpdateDTO.class);
            StoreResponseDTO res = storeService.updateStoreByDto(dto);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(res);

        } catch (JacksonException e) {
            throw new BadRequestException(ErrorCodeMessage.REQUEST_NOT_INVALID);
        }
    }

    @PostMapping("/image-upload")
    public ResponseEntity<Void> imageUpload(@Valid @ModelAttribute StoreImageUploadRequestDTO dto) {
        storeService.imageUpload(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/list")
    public ResponseEntity<Page<StoreResponseDTO>> findStoreList(
            @RequestParam(value = "sigungu", defaultValue = "") String sigungu,
            @RequestParam(value = "category", defaultValue = "NONE") CategoryType category,
            @RequestParam(value = "kw", defaultValue = "") String kw,
            @PageableDefault(size = 16, sort = "id", direction = Sort.Direction.DESC ) Pageable pageable)
    {
        Page<StoreResponseDTO> res = storeService.findStoreList(sigungu, category, kw, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }
}

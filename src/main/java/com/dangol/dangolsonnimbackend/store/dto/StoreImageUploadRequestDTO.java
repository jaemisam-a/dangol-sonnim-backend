package com.dangol.dangolsonnimbackend.store.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreImageUploadRequestDTO {
    private List<MultipartFile> multipartFile;
    private Long storeId;
}

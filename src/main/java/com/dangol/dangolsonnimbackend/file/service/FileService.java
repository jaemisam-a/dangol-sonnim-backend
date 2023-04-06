package com.dangol.dangolsonnimbackend.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile multipartFile);
    void fileDelete(String imageUrl);
}

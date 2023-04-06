package com.dangol.dangolsonnimbackend.file.service.serviceImpl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private AmazonS3Client amazonS3Client;

    @InjectMocks
    private FileServiceImpl fileService;

    private String bucket = "dangol";
    private String bucketUrl = "https://dangol.s3.amazonaws.com/";
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        fileService.bucket = bucket;
        fileService.bucketUrl = bucketUrl;
        mockFile = new MockMultipartFile("file", "test.jpg",
                MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
    }

    @Test
    void upload() throws IOException {
        when(amazonS3Client.putObject(any(PutObjectRequest.class))).thenReturn(null);
        when(amazonS3Client.getUrl(eq(bucket), anyString())).thenReturn(new URL(bucketUrl + "static/test.jpg"));

        String resultUrl = fileService.upload(mockFile);

        verify(amazonS3Client, times(1)).putObject(any(PutObjectRequest.class));
        verify(amazonS3Client, times(1)).getUrl(eq(bucket), anyString());
    }

    @Test
    void fileDelete() {
        doNothing().when(amazonS3Client).deleteObject(anyString(), anyString());

        fileService.fileDelete(bucketUrl + "test.jpg");

        verify(amazonS3Client, times(1)).deleteObject(anyString(), anyString());
    }
}
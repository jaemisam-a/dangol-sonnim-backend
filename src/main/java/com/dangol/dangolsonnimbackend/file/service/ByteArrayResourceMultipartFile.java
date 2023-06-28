package com.dangol.dangolsonnimbackend.file.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ByteArrayResourceMultipartFile extends ByteArrayResource implements MultipartFile {
    private String filename;
    private byte[] byteArray;

    public ByteArrayResourceMultipartFile(byte[] byteArray, String filename) {
        super(byteArray);
        this.filename = filename;
        this.byteArray = byteArray;
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getName() {
        return filename;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return byteArray.length;
    }

    @Override
    public byte[] getBytes() {
        return byteArray;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(byteArray);
    }

    @Override
    public Resource getResource() {
        return MultipartFile.super.getResource();
    }

    @Override
    public void transferTo(File dest) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        MultipartFile.super.transferTo(dest);
    }
}

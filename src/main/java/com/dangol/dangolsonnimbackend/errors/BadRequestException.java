package com.dangol.dangolsonnimbackend.errors;

import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCode;

public class BadRequestException extends RuntimeException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}

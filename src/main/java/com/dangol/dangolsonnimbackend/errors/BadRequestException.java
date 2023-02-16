package com.dangol.dangolsonnimbackend.errors;

import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;

public class BadRequestException extends RuntimeException {

    public BadRequestException(ErrorCodeMessage errorCodeMessage) {
        super(errorCodeMessage.getMessage());
    }
}

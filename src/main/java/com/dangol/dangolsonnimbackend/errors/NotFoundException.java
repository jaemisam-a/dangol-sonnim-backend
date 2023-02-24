package com.dangol.dangolsonnimbackend.errors;

import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;

public class NotFoundException extends RuntimeException{
    public NotFoundException(ErrorCodeMessage errorCodeMessage) {
        super(errorCodeMessage.getMessage());
    }
}

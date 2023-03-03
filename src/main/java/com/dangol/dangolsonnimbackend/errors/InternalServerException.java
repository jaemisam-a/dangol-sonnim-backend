package com.dangol.dangolsonnimbackend.errors;

import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;

public class InternalServerException extends RuntimeException {

    public InternalServerException(ErrorCodeMessage errorCodeMessage) {
        super(errorCodeMessage.getMessage());
    }
}
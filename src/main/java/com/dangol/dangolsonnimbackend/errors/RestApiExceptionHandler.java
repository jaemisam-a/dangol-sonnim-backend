package com.dangol.dangolsonnimbackend.errors;

import com.dangol.dangolsonnimbackend.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestApiExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ApiResponse<Object> handleBadRequestException(BadRequestException ex) {
        log.info("!!!Bad Request Error, [{}], message={}", MDC.get("UUID"), ex.getMessage());
        return ApiResponse.clientFail("message", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

package com.dangol.dangolsonnimbackend.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class RestApiExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        // requestId 는 에러 로그를 식별하기 위해 사용됩니다.
        String requestId = UUID.randomUUID().toString();
        log.info("Bad Request Error, requestId={}, message={}", requestId, ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), requestId);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @Getter
    @AllArgsConstructor
    private class ErrorResponse {
        private String message;
        private String requestId;
    }
}

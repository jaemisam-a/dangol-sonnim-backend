package com.dangol.dangolsonnimbackend.errors;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestApiExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        // MDC 는 에러 로그를 식별하기 위해 사용됩니다.
        String requestId = MDC.get("UUID");
        log.info("Bad Request Error, requestId={}, message={}", requestId, ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), requestId);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private class ErrorResponse {
        private String message;
        private String requestId;

        public ErrorResponse(String message, String requestId) {
            this.message = message;
            this.requestId = requestId;
        }
    }
}

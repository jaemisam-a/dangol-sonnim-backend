package com.dangol.dangolsonnimbackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final static int SUCCESS = 200;
    private final static String SUCCESS_MESSAGE = "SUCCESS";
    private final static String FAILED_MESSAGE = "FAILED";

    private final ApiResponseHeader header;

    private final Map<String, T> body;

    public static <T> ApiResponse<T> success(String name, T body) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);

        return new ApiResponse<>(new ApiResponseHeader(SUCCESS, SUCCESS_MESSAGE), map);
    }

    public static <T> ApiResponse<T> clientFail(String name, T body, HttpStatus httpStatus) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);
        return new ApiResponse<>(new ApiResponseHeader(httpStatus.value(), FAILED_MESSAGE), map);
    }
}

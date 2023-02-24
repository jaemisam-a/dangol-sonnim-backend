package com.dangol.dangolsonnimbackend.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AsyncConfigTest {

    @Autowired
    private AsyncConfig asyncConfig;

    @Test
    public void testAsyncExecutor() throws Exception {
        Future<String> result = asyncConfig.getAsyncExecutor().submit(() -> {
            Thread.sleep(1000);
            return "Hello, world!";
        });

        // 비동기 실행이 완료될 때까지 대기
        while (!result.isDone()) {
            Thread.sleep(100);
        }

        assertEquals(result.get(),"Hello, world!");
    }
}
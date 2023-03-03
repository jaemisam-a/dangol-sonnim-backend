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

        String actualResult = result.get();

        assertEquals(actualResult, "Hello, world!");
    }
}
package com.dangol.dangolsonnimbackend.oauth;

public class TokenValidFailedException extends RuntimeException {

    public TokenValidFailedException() {
        super("Failed to generate Token.");
    }
}
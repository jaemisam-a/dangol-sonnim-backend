package com.dangol.dangolsonnimbackend.subscribe.enumeration;

import lombok.Getter;

@Getter
public enum SubscribeType {
    MONTHLY("월간 결제"),
    COUNT("횟수 결제");

    private final String kind;

    SubscribeType(String kind){
        this.kind = kind;
    }
}

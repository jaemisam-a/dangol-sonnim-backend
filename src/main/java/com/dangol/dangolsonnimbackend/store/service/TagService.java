package com.dangol.dangolsonnimbackend.store.service;

import com.dangol.dangolsonnimbackend.store.domain.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    Set<Tag> getOrCreateTags(List<String> tagNames);
}

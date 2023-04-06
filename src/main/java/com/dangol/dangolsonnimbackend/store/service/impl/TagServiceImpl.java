package com.dangol.dangolsonnimbackend.store.service.impl;

import com.dangol.dangolsonnimbackend.store.domain.Tag;
import com.dangol.dangolsonnimbackend.store.repository.TagRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.TagQueryRepository;
import com.dangol.dangolsonnimbackend.store.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class TagServiceImpl implements TagService{

    private final TagQueryRepository tagQueryRepository;
    private final TagRepository tagRepository;

    public TagServiceImpl(TagQueryRepository tagQueryRepository, TagRepository tagRepository) {
        this.tagQueryRepository = tagQueryRepository;
        this.tagRepository = tagRepository;
    }


    public Set<Tag> getOrCreateTags(List<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagQueryRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }
        return tags;
    }
}

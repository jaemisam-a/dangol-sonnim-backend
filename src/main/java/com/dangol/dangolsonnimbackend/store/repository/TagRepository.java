package com.dangol.dangolsonnimbackend.store.repository;

import com.dangol.dangolsonnimbackend.store.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}

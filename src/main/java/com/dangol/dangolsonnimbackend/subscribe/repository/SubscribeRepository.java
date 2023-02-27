package com.dangol.dangolsonnimbackend.subscribe.repository;

import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository <T extends Subscribe> extends JpaRepository<T, Long> {}


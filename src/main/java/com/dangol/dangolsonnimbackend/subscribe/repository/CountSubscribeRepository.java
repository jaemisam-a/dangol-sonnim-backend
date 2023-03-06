package com.dangol.dangolsonnimbackend.subscribe.repository;

import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountSubscribeRepository extends JpaRepository<CountSubscribe, Long> {
}

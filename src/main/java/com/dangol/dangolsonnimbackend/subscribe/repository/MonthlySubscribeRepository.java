package com.dangol.dangolsonnimbackend.subscribe.repository;

import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlySubscribeRepository extends JpaRepository<MonthlySubscribe, Long> {
}

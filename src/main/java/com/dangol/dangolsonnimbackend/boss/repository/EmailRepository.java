package com.dangol.dangolsonnimbackend.boss.repository;

import com.dangol.dangolsonnimbackend.boss.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmailRepository extends JpaRepository<Email, Long> {

}

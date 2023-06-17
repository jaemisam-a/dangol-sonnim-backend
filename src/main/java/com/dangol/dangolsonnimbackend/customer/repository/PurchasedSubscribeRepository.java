package com.dangol.dangolsonnimbackend.customer.repository;

import com.dangol.dangolsonnimbackend.customer.domain.PurchasedSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedSubscribeRepository extends JpaRepository<PurchasedSubscribe, Long> {}

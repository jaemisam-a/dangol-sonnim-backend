package com.dangol.dangolsonnimbackend.subscribe.repository;

import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository <T extends Subscribe> extends JpaRepository<T, Long> {
    @Query(value = "SELECT DISTINCT s FROM Subscribe s LEFT JOIN PurchasedSubscribe p " + "ON s.id = p.subscribe.id WHERE p.customer.id = ?1")
    List<Subscribe> findAllPurchasedSubscribeByCustomerId(String customerId);
}


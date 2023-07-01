package com.dangol.dangolsonnimbackend.customer.repository;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.domain.PurchasedSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasedSubscribeRepository extends JpaRepository<PurchasedSubscribe, Long> {
    List<PurchasedSubscribe> findAllByCustomer(Customer customer);

    PurchasedSubscribe findByIdAndCustomer(Long id, Customer customer);
}

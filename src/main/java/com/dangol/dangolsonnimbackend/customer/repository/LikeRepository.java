package com.dangol.dangolsonnimbackend.customer.repository;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.domain.Like;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByCustomerAndStore(Customer customer, Store store);
}

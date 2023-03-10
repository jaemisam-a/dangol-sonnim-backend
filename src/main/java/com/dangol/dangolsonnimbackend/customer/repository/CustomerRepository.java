package com.dangol.dangolsonnimbackend.customer.repository;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.type.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<Customer> findByEmail(String email);
}
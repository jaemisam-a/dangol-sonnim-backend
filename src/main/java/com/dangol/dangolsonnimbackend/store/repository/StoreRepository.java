package com.dangol.dangolsonnimbackend.store.repository;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findAllBySigunguContainingAndCategory_CategoryTypeAndNameContaining(String sigungu, CategoryType category, String kw, Pageable pageable);
    Page<Store> findAllBySigunguContainingAndNameContaining(String sigungu, String name, Pageable pageable);
    @Query(value = "SELECT s FROM Store s LEFT JOIN FETCH Like l " + "ON l.store.id = s.id WHERE l.customer = ?1")
    List<Store> findAllByLikedStore(Customer customer);

}

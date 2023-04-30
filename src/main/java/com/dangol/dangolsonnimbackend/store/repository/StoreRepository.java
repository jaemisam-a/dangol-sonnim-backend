package com.dangol.dangolsonnimbackend.store.repository;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findAllBySigunguContainingAndCategory_CategoryTypeAndNameContaining(String sigungu, CategoryType category, String kw, Pageable pageable);
}

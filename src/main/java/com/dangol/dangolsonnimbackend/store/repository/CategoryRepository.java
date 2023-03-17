package com.dangol.dangolsonnimbackend.store.repository;

import com.dangol.dangolsonnimbackend.store.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

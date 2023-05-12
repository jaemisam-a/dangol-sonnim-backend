package com.dangol.dangolsonnimbackend.store.repository.dsl;

import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CategoryQueryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryQueryRepository categoryQueryRepository;

    @Test
    @DisplayName("카테고리 테이블에 새로운 카테고리 정보가 추가되면 정상적으로 조회되어야 한다.")
    void givenSignUpDTO_whenFindById_thenReturnCategory() {
        Category category = new Category();
        category.setCategoryType(CategoryType.KOREAN);
        categoryRepository.save(category);

        Category foundCategory = categoryQueryRepository.findByCategoryType(category.getCategoryType());
        Assertions.assertNotNull(foundCategory);
    }
}
package com.dangol.dangolsonnimbackend.store.repository;

import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.linesOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Store store;

    private Category category;

    private StoreSignupRequestDTO dto;

    @BeforeEach
    void setUp() {
        dto = StoreSignupRequestDTO.builder()
                .name("단골손님" + new Random().nextInt())
                .phoneNumber("01012345678")
                .newAddress("서울특별시 서초구 단골로 130")
                .sido("서울특별시")
                .sigungu("서초구")
                .bname1("단골동")
                .bname2("")
                .detailedAddress("")
                .comments("단골손님 가게로 좋아요.")
                .officeHours("08:00~10:00")
                .categoryType(CategoryType.KOREAN)
                .registerNumber("1234567890")
                .registerName("단골손님")
                .build();

        category = new Category();
        category.setCategoryType(dto.getCategoryType());
        categoryRepository.save(category);

        store = new Store(dto, category);
        storeRepository.save(store);
    }

    @Test
    @DisplayName("카테고리 정보가 포함되어 생성된 가게는 정상적으로 조회되어야 한다.")
    void whenCreateNewStore_thenReturnStoreAndCategory() {
        Optional<Store> store = storeRepository.findById(this.store.getId());
        Optional<Category> category = categoryRepository.findById(this.store.getCategory().getId());

        assertTrue(store.isPresent());
        assertTrue(category.isPresent());

        category.ifPresent(found -> {
            System.out.println(found);
            System.out.println(found.getStore().size());
            assertTrue(found.getStore().contains(store.get()));
        });
    }

    @Test
    @DisplayName("카테고리 정보가 포함되어 수정된 가게는 정상적으로 반영되어야 한다.")
    void whenUpdateStore_thenUpdateCategoryAndCheckCategory() {
        Optional<Store> store = storeRepository.findById(this.store.getId());

        store.ifPresent(found -> {
            Category newCategory = new Category();
            newCategory.setCategoryType(CategoryType.CHINESE);
            categoryRepository.save(newCategory);

            found.updateCategory(newCategory);
            storeRepository.save(found);
        });

        Store updatedStore = storeRepository.getById(this.store.getId());
        Category updatedCategory = categoryRepository.getById(this.store.getCategory().getId());

        assertThat(updatedStore.getCategory().getCategoryType()).isEqualTo(CategoryType.CHINESE);
        assertThat(updatedCategory.getStore().contains(updatedStore));
    }
}

package com.dangol.dangolsonnimbackend.store.repository;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.request.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;

    private Store store;

    private StoreSignupRequestDTO dto;
    private static final String BOSS_TEST_NAME = "GilDong";
    private static final String BOSS_TEST_EMAIL = "test@example.com";
    private static final String BOSS_TEST_PASSWORD = "password";
    private static final String BOSS_TEST_PHONE_NUMBER = "01012345678";
    private static final Boolean BOSS_TEST_MARKETING_AGREEMENT = true;
    @Autowired
    private BossService bossService;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        BossSignupRequestDTO bossSignupRequestDTO = new BossSignupRequestDTO();
        bossSignupRequestDTO.setName(BOSS_TEST_NAME);
        bossSignupRequestDTO.setEmail(BOSS_TEST_EMAIL);
        bossSignupRequestDTO.setPassword(BOSS_TEST_PASSWORD);
        bossSignupRequestDTO.setPhoneNumber(BOSS_TEST_PHONE_NUMBER);
        bossSignupRequestDTO.setMarketingAgreement(BOSS_TEST_MARKETING_AGREEMENT);
        bossService.signup(bossSignupRequestDTO);
        Boss boss = bossService.findByEmail(BOSS_TEST_EMAIL);

        StoreSignupRequestDTO dto = StoreSignupRequestDTO.builder()
                .name("단골손님")
                .phoneNumber("01012345678")
                .newAddress("서울특별시 서초구 단골로 130")
                .sido("서울특별시")
                .sigungu("서초구")
                .bname1("단골동")
                .detailedAddress("")
                .comments("단골손님 가게로 좋아요.")
                .officeHours("08:00~10:00")
                .registerNumber("123-456-789")
                .registerName("단골손님")
                .tags(List.of("태그1", "태그2"))
                .build();
        Category category = new Category();
        category.setCategoryType(dto.getCategoryType());
        categoryRepository.save(category);

        store = new Store(dto, category, boss);
        storeRepository.save(store);
    }

    @Test
    @DisplayName("가게 ID 값을 통해 가게를 조회한다.")
    void whenFindById_thenReturnStore() {
        Optional<Store> found = storeRepository.findById(store.getId());
        assertTrue(found.isPresent());
        assertEquals(store, found.get());
    }

    @Test
    @DisplayName("가게 ID 값을 통해 가게를 수정한다.")
    void whenFindById_thenUpdateAndCheckStore() {
        Store updatedStore = null;
        Optional<Store> found = storeRepository.findById(store.getId());

        if(found.isPresent()) {
            store.updateName("그냥손님");
            updatedStore = storeRepository.save(store);
        }

        assertEquals(updatedStore.getName(), "그냥손님");
    }

    @Test
    @DisplayName("가게 ID 값을 통해 가게를 삭제한다.")
    void whenFindById_thenDeleteStore() {
        Optional<Store> found = storeRepository.findById(store.getId());

        if(found.isPresent()) {
            storeRepository.delete(store);
        }

        assertFalse(storeRepository.findById(store.getId()).isPresent());
    }
}
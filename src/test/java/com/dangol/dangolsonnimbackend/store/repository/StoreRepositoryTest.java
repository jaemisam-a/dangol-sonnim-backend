package com.dangol.dangolsonnimbackend.store.repository;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.repository.dsl.StoreQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
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

    @BeforeEach
    void setUp() {
        dto = StoreSignupRequestDTO.builder()
                .name("단골손님" + new Random().nextInt())
                .storePhoneNumber("01012345678")
                .newAddress("서울특별시 서초구 단골로 130")
                .sido("서울특별시")
                .sigungu("서초구")
                .bname1("단골동")
                .bname2("")
                .detailedAddress("")
                .comments("단골손님 가게로 좋아요.")
                .officeHours("08:00~10:00")
                .categoryId(1L)
                .storeRegisterNumber("1234567890")
                .storeRegisterName("단골손님")
                .build();

        store = new Store(dto);
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
package com.dangol.dangolsonnimbackend.store.repository.dsl;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StoreQueryRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreQueryRepository storeQueryRepository;

    private Store store;

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
                .registerNumber("1234567890")
                .registerName("단골손님")
                .build();

        store = new Store(dto);
        storeRepository.save(store);
    }

    @Test
    @DisplayName("가게 사업자 번호가 중복된 경우를 확인한다.")
    void givenValidSrn_whenExistsBySrn_thenReturnTrue() {
        Boolean result = storeQueryRepository.existsByRegisterNumber("1234567890");

        assertThat(result).isTrue();
    }
}
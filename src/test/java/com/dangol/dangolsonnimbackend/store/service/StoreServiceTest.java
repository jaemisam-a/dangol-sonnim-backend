package com.dangol.dangolsonnimbackend.store.service;

import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.dto.StoreResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.CategoryRepository;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.StoreQueryRepository;
import com.dangol.dangolsonnimbackend.store.service.impl.StoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class StoreServiceTest {
    @Autowired
    private StoreQueryRepository storeQueryRepository;

    @Autowired
    private StoreServiceImpl storeService;

    @Autowired
    private CategoryRepository categoryRepository;

    private StoreSignupRequestDTO dto;

    @BeforeEach
    public void setup() {
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
                .categoryType(CategoryType.KOREAN)
                .build();

        categoryRepository.saveAndFlush(new Category(CategoryType.KOREAN));
        categoryRepository.saveAndFlush(new Category(CategoryType.CHINESE));
    }

    @Test
    @DisplayName("새로운 가게 정보를 전달받으면 가게를 생성하도록 한다.")
    public void givenRequestDto_whenSignup_thenSaveStore() {
        StoreResponseDTO response = storeService.create(dto);

        assertEquals(response.getName(), dto.getName());
        assertEquals(response.getNewAddress(), dto.getNewAddress());
        assertEquals(response.getCategoryType(), dto.getCategoryType());
    }

    @Test
    @DisplayName("가게 ID 값을 전달받으면 가게를 정상적으로 조회할 수 있다")
    public void givenStoreId_whenFindById_thenReturnStore() {
        StoreResponseDTO response = storeService.create(dto);

        assertTrue(storeQueryRepository.findById(response.getId()).isPresent());
    }

    @Test
    @DisplayName("수정할 가게 정보를 전달받으면 가게를 수정하도록 한다.")
    public void givenUpdateDto_whenFindByRegisterNumber_thenUpdateStore() {
        storeService.create(dto);

        StoreUpdateDTO updateDto =
                new StoreUpdateDTO(dto.getRegisterNumber())
                        .name("단골손님" + new Random().nextInt())
                        .sido("부산광역시")
                        .categoryType(CategoryType.CHINESE);

        StoreResponseDTO response = storeService.updateStoreByDto(updateDto);

        assertNotEquals(dto.getName(), storeQueryRepository.findById(response.getId()).get().getName());
        assertNotEquals(dto.getSido(), storeQueryRepository.findById(response.getId()).get().getSido());
        assertEquals(CategoryType.CHINESE, response.getCategoryType());
    }

}

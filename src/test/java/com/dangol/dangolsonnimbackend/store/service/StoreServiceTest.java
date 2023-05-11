package com.dangol.dangolsonnimbackend.store.service;

import com.dangol.dangolsonnimbackend.boss.dto.request.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.BusinessHourRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.CategoryRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.StoreQueryRepository;
import com.dangol.dangolsonnimbackend.store.service.impl.StoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
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
    private static final String BOSS_TEST_NAME = "GilDong";
    private static final String BOSS_TEST_EMAIL = "test@example.com";
    private static final String BOSS_TEST_PASSWORD = "password";
    private static final String BOSS_TEST_PHONE_NUMBER = "01012345678";
    private static final Boolean BOSS_TEST_MARKETING_AGREEMENT = true;
    @Autowired
    private BossService bossService;

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
                .registerNumber("1234567890")
                .registerName("단골손님")
                .categoryType(CategoryType.KOREAN)
                .tags(List.of("태그1", "태그2"))
                .businessHours(List.of(new BusinessHourRequestDTO("월~수", "10:00~12:00"),
                        new BusinessHourRequestDTO("토,일", "10:00~12:00")))
                .build();

        BossSignupRequestDTO bossSignupRequestDTO = new BossSignupRequestDTO();
        bossSignupRequestDTO.setName(BOSS_TEST_NAME);
        bossSignupRequestDTO.setEmail(BOSS_TEST_EMAIL);
        bossSignupRequestDTO.setPassword(BOSS_TEST_PASSWORD);
        bossSignupRequestDTO.setPhoneNumber(BOSS_TEST_PHONE_NUMBER);
        bossSignupRequestDTO.setMarketingAgreement(BOSS_TEST_MARKETING_AGREEMENT);
        bossService.signup(bossSignupRequestDTO);
    }

    @Test
    @DisplayName("새로운 가게 정보를 전달받으면 가게를 생성하도록 한다.")
    void givenRequestDto_whenSignup_thenSaveStore() {
        StoreResponseDTO response = storeService.create(dto, BOSS_TEST_EMAIL);

        assertEquals(response.getName(), dto.getName());
        assertEquals(response.getNewAddress(), dto.getNewAddress());
        assertEquals(response.getCategoryType(), dto.getCategoryType());
    }

    @Test
    @DisplayName("가게 ID 값을 전달받으면 가게를 정상적으로 조회할 수 있다")
    void givenStoreId_whenFindById_thenReturnStore() {
        StoreResponseDTO response = storeService.create(dto, BOSS_TEST_EMAIL);

        assertTrue(storeQueryRepository.findById(response.getId()).isPresent());
    }

    @Test
    @DisplayName("수정할 가게 정보를 전달받으면 가게를 수정하도록 한다.")
    void givenUpdateDto_whenFindByRegisterNumber_thenUpdateStore() {
        StoreResponseDTO responseDTO = storeService.create(dto, BOSS_TEST_EMAIL);
        Category category = new Category();
        category.setCategoryType(CategoryType.CHINESE);
        categoryRepository.save(category);

        StoreUpdateDTO updateDto =
                new StoreUpdateDTO(dto.getRegisterNumber())
                        .name("단골손님" + new Random().nextInt())
                        .sido("부산광역시")
                        .categoryType(CategoryType.CHINESE);

        StoreResponseDTO response = storeService.updateStoreByDto(updateDto, responseDTO.getId());

        assertNotEquals(dto.getName(), storeQueryRepository.findById(response.getId()).get().getName());
        assertNotEquals(dto.getSido(), storeQueryRepository.findById(response.getId()).get().getSido());
        assertEquals(CategoryType.CHINESE, response.getCategoryType());
    }

}

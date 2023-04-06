package com.dangol.dangolsonnimbackend.subscribe.repository;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.request.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.BusinessHourRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.repository.CategoryRepository;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CountSubscribeRepositoryTest {
    @Autowired
    private SubscribeRepository<CountSubscribe> subscribeRepository;
    @Autowired
    private StoreRepository storeRepository;

    private static final String BOSS_TEST_NAME = "GilDong";
    private static final String BOSS_TEST_EMAIL = "test@example.com";
    private static final String BOSS_TEST_PASSWORD = "password";
    private static final String BOSS_TEST_PHONE_NUMBER = "01012345678";
    private static final Boolean BOSS_TEST_MARKETING_AGREEMENT = true;
    @Autowired
    private BossService bossService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Test
    void givenCountSubscribe_whenSaveCountSubscribe_thenSuccess() {
        // given
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
                .registerNumber("123-456-789")
                .registerName("단골손님")
                .tags(List.of("태그1", "태그2"))
                .businessHours(List.of(new BusinessHourRequestDTO("월~수", "10:00~12:00"),
                        new BusinessHourRequestDTO("토,일", "10:00~12:00")))
                .build();
        Category category = new Category();
        category.setCategoryType(dto.getCategoryType());
        categoryRepository.save(category);

        Store store = new Store(dto, category, boss);
        storeRepository.save(store);

        Integer useCount = 10;
        SubscribeRequestDTO subscribeRequestDTO = SubscribeRequestDTO.builder()
                .name("SubscribeName")
                .price(new BigDecimal(10000))
                .intro("SubscribeIntro")
                .isTop(true)
                .useCount(useCount)
                .build();

        // when
        CountSubscribe savedCountSubscribe = subscribeRepository.save(new CountSubscribe(subscribeRequestDTO, store));

        // then
        assertThat(savedCountSubscribe.getId()).isNotNull();
        assertThat(savedCountSubscribe.getName()).isEqualTo("SubscribeName");
        assertThat(savedCountSubscribe.getPrice()).isEqualByComparingTo(new BigDecimal(10000));
        assertThat(savedCountSubscribe.getIntro()).isEqualTo("SubscribeIntro");
        assertThat(savedCountSubscribe.getIsTop()).isTrue();
        assertThat(savedCountSubscribe.getStore()).isEqualTo(store);
        assertThat(savedCountSubscribe.getUseCount()).isEqualTo(useCount);
        assertThat(savedCountSubscribe.getCreatedAt()).isNotNull();
    }
}
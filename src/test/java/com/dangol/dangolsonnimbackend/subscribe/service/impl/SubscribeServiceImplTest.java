package com.dangol.dangolsonnimbackend.subscribe.service.impl;

import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.CategoryRepository;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import com.dangol.dangolsonnimbackend.subscribe.dto.BenefitDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
import com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType;
import com.dangol.dangolsonnimbackend.subscribe.repository.SubscribeRepository;
import com.dangol.dangolsonnimbackend.subscribe.service.SubscribeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SubscribeServiceImplTest {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private SubscribeRepository<Subscribe> subscribeRepository;
    @Autowired
    private SubscribeService subscribeService;
    @Autowired
    private CategoryRepository categoryRepository;
    private SubscribeRequestDTO monthlySubscribeRequestDTO;
    private SubscribeRequestDTO countSubscribeRequestDTO;

    @BeforeEach
    void setup(){
        StoreSignupRequestDTO signupRequestDTO = StoreSignupRequestDTO.builder()
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
        Store store = storeRepository.save(new Store(signupRequestDTO));

        Category category = new Category();
        category.setCategoryType(CategoryType.KOREAN);
        categoryRepository.save(category);

        List<BenefitDTO> benefitDTOList = List.of(
                new BenefitDTO("Benefit 1"),
                new BenefitDTO("Benefit 2"),
                new BenefitDTO("Benefit 3")
        );

        monthlySubscribeRequestDTO = SubscribeRequestDTO.builder()
                .type(SubscribeType.MONTHLY)
                .storeId(store.getId())
                .name("SubscribeName")
                .price(BigDecimal.valueOf(12000))
                .intro("SubscribeIntro")
                .isTop(true)
                .benefits(benefitDTOList)
                .build();
        countSubscribeRequestDTO = SubscribeRequestDTO.builder()
                .type(SubscribeType.COUNT)
                .storeId(store.getId())
                .name("SubscribeName")
                .price(BigDecimal.valueOf(12000))
                .intro("SubscribeIntro")
                .isTop(true)
                .benefits(benefitDTOList)
                .useCount(100)
                .build();
    }

    @Test
    void givenSubscribeRequestDTO_whenCreate_thenCreateMonthlySubscribe() {
        // given

        // when
        SubscribeResponseDTO responseDTO = subscribeService.create(monthlySubscribeRequestDTO);

        // then
        Optional<Subscribe> optionalSubscribe = subscribeRepository.findById(responseDTO.getSubscribeId());
        MonthlySubscribe createdSubscribe = (MonthlySubscribe) optionalSubscribe.orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.SUBSCRIBE_NOT_FOUND));
        assertNotNull(createdSubscribe);
        assertNotNull(createdSubscribe.getCreatedAt());
    }

    @Test
    void givenSubscribeRequestDTO_whenCreate_thenCreateCountSubscribe() {
        // given

        // when
        SubscribeResponseDTO responseDTO = subscribeService.create(countSubscribeRequestDTO);

        // then
        Optional<Subscribe> optionalSubscribe = subscribeRepository.findById(responseDTO.getSubscribeId());
        CountSubscribe createdSubscribe = (CountSubscribe) optionalSubscribe.orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.SUBSCRIBE_NOT_FOUND));
        assertNotNull(createdSubscribe);
        assertEquals(countSubscribeRequestDTO.getUseCount(), createdSubscribe.getUseCount());
    }

    @Test
    void givenSubscribeId_whenGetSubscribe_thenReturnSubscribeResponseDTO(){
        // given
        Long subscribeId = subscribeService.create(countSubscribeRequestDTO).getSubscribeId();
        // when
        SubscribeResponseDTO responseDTO = subscribeService.getSubscribe(subscribeId);

        // then
        Optional<Subscribe> optionalSubscribe = subscribeRepository.findById(responseDTO.getSubscribeId());
        CountSubscribe createdSubscribe = (CountSubscribe) optionalSubscribe.orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.SUBSCRIBE_NOT_FOUND));
        assertNotNull(createdSubscribe);
        assertEquals(countSubscribeRequestDTO.getUseCount(), createdSubscribe.getUseCount());
    }

    @Test
    void givenSubscribeId_whenDeleteSubscribe_thenSuccess(){
        // given
        Long subscribeId = subscribeService.create(countSubscribeRequestDTO).getSubscribeId();
        // when
        subscribeService.deleteSubscribe(subscribeId);
        // then

        assertNull(subscribeRepository.findById(subscribeId).orElse(null));
    }
}
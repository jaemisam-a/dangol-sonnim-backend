package com.dangol.dangolsonnimbackend.subscribe.service.impl;

import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
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
import java.util.Optional;

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
    private Store store;

    @BeforeEach
    void setup(){
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
                .build();
        store = storeRepository.save(new Store(dto));
    }

    @Test
    void givenSubscribeRequestDTO_whenCreate_thenCreateMonthlySubscribe() {
        // given
        SubscribeRequestDTO subscribeRequestDTO = SubscribeRequestDTO.builder()
                .type(SubscribeType.MONTHLY)
                .storeId(store.getId())
                .name("SubscribeName")
                .price(new BigDecimal(10000))
                .intro("SubscribeIntro")
                .isTop(true)
                .useCount(5)
                .build();

        // when
        SubscribeResponseDTO responseDTO = subscribeService.create(subscribeRequestDTO);

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
        SubscribeRequestDTO subscribeRequestDTO = SubscribeRequestDTO.builder()
                .type(SubscribeType.COUNT)
                .storeId(store.getId())
                .name("SubscribeName")
                .price(new BigDecimal(10000))
                .intro("SubscribeIntro")
                .isTop(true)
                .useCount(5)
                .build();


        // when
        SubscribeResponseDTO responseDTO = subscribeService.create(subscribeRequestDTO);

        // then
        Optional<Subscribe> optionalSubscribe = subscribeRepository.findById(responseDTO.getSubscribeId());
        CountSubscribe createdSubscribe = (CountSubscribe) optionalSubscribe.orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.SUBSCRIBE_NOT_FOUND));
        assertNotNull(createdSubscribe);
        assertEquals(subscribeRequestDTO.getUseCount(), createdSubscribe.getUseCount());
    }
}
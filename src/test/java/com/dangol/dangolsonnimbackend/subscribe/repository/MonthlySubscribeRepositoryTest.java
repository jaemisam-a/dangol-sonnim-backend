package com.dangol.dangolsonnimbackend.subscribe.repository;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
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
class MonthlySubscribeRepositoryTest {
    @Autowired
    private SubscribeRepository<MonthlySubscribe> subscribeRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    void givenMonthlySubscribe_whenSaveMonthlySubscribe_thenSuccess() {
        // given
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
        Store store = storeRepository.save(new Store(dto));

        SubscribeRequestDTO subscribeRequestDTO = SubscribeRequestDTO.builder()
                .name("SubscribeName")
                .price(new BigDecimal(10000))
                .intro("SubscribeIntro")
                .isTop(true)
                .build();

        // when
        MonthlySubscribe savedMonthlySubscribe = subscribeRepository.save(new MonthlySubscribe(subscribeRequestDTO, store));

        // then
        assertThat(savedMonthlySubscribe.getId()).isNotNull();
        assertThat(savedMonthlySubscribe.getName()).isEqualTo("SubscribeName");
        assertThat(savedMonthlySubscribe.getPrice()).isEqualByComparingTo(new BigDecimal(10000));
        assertThat(savedMonthlySubscribe.getIntro()).isEqualTo("SubscribeIntro");
        assertThat(savedMonthlySubscribe.getIsTop()).isTrue();
        assertThat(savedMonthlySubscribe.getStore()).isEqualTo(store);
    }
}
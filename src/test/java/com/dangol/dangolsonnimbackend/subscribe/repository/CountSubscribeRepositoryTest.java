package com.dangol.dangolsonnimbackend.subscribe.repository;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
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

    @Test
    void givenCountSubscribe_whenSaveCountSubscribe_thenSuccess() {
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
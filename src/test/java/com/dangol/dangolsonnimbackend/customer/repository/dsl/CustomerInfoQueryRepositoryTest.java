package com.dangol.dangolsonnimbackend.customer.repository.dsl;

import com.dangol.dangolsonnimbackend.customer.domain.CustomerInfo;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupRequestDTO;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class CustomerInfoQueryRepositoryTest {

    @Autowired
    private CustomerInfoRepository customerInfoRepository;
    @Autowired
    private CustomerInfoQueryRepository customerInfoQueryRepository;

    @BeforeEach
    public void setUp() {

        CustomerSignupRequestDTO dto = new CustomerSignupRequestDTO();

        dto.setNickname("후추");
        dto.setProfileImageUrl("https:/test.com");
        dto.setPhoneNumber("01012345678");
        dto.setBirth("19900101");

        customerInfoRepository.save(new CustomerInfo(dto));
    }

    @Test
    @DisplayName("닉네임 중복 체크")
    void givenNickname_whenExistsByNickname_thenReturnTrue() {
        // given

        // when
        boolean existsByNickname = customerInfoQueryRepository.existsByNickname("후추");

        // then
        assert existsByNickname;
    }

    @Test
    @DisplayName("이메일 중복 체크 - False")
    void givenPhoneNumber_whenExistsByPhoneNumber_thenReturnFalse() {
        // given

        // when
        boolean existsByPhoneNumber = customerInfoQueryRepository.existsByPhoneNumber("01012345679");

        // then
        assertThat(existsByPhoneNumber).isFalse();
    }
}

package com.dangol.dangolsonnimbackend.customer.repository.dsl;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupDTO;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class CustomerQueryRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerQueryRepository customerQueryRepository;

    @BeforeEach
    public void setUp() {

        CustomerSignupDTO dto = new CustomerSignupDTO();

        dto.setName("이현지");
        dto.setNickname("후추맘");
        dto.setProfileImageUrl("https:/test.com");
        dto.setPhoneNumber("01012345678");
        dto.setBirth("19900101");
        dto.setMarketingAgreement(true);

        customerRepository.save(new Customer(dto));
    }

    @Test
    @DisplayName("닉네임 중복 체크")
    void givenNickname_whenExistsByNickname_thenReturnTrue() {
        // given

        // when
        boolean existsByNickname = customerQueryRepository.existsByNickname("후추");

        // then
        assert existsByNickname;
    }

    @Test
    @DisplayName("이메일 중복 체크 - False")
    void givenPhoneNumber_whenExistsByPhoneNumber_thenReturnFalse() {
        // given

        // when
        boolean existsByPhoneNumber = customerQueryRepository.existsByPhoneNumber("01012345679");

        // then
        assertThat(existsByPhoneNumber).isFalse();
    }
}

package com.dangol.dangolsonnimbackend.customer.repository.dsl;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.type.RoleType;
import com.dangol.dangolsonnimbackend.type.SocialType;
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

    private Customer customer;

    @BeforeEach
    public void setUp() {

        customer = Customer.builder()
                .name("")
                .nickname("")
                .email("test@email.com")
                .profileImageUrl("test.url")
                .phoneNumber("")
                .birth("")
                .marketingAgreement(false)
                .roleType(RoleType.GUEST)
                .socialId("154352234114")
                .socialType(SocialType.KAKAO)
                .build();

        customerRepository.save(customer);
    }

    @Test
    @DisplayName("닉네임 중복 체크")
    void givenNickname_whenExistsByNickname_thenReturnTrue() {
        // given

        // when
        boolean existsByNickname = customerQueryRepository.existsByNickname("");

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

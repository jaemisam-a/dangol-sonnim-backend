package com.dangol.dangolsonnimbackend.customer.repository;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupDTO;
import com.dangol.dangolsonnimbackend.type.RoleType;
import com.dangol.dangolsonnimbackend.type.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerSignupDTO dto;

    private Customer customer;

    @BeforeEach
    void setUp() {

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
    }

    @Test
    void whenFindById_thenReturnCustomer() {
        customer = customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findById(customer.getId());

        assertTrue(foundCustomer.isPresent());
        assertEquals(customer, foundCustomer.get());
    }
}

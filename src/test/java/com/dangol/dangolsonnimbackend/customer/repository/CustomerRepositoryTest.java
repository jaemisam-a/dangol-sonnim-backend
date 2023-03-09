package com.dangol.dangolsonnimbackend.customer.repository;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerSignupDTO dto;

    @BeforeEach
    public void setup() {
        dto = new CustomerSignupDTO();

        dto.setName("후추");
        dto.setNickname("후추");
        dto.setProfileImageUrl("https:/test.com");
        dto.setPhoneNumber("01012345678");
        dto.setBirth("19900101");
        dto.setMarketingAgreement(true);
    }

    @Test
    void givenSignupDTO_whenSave_thenReturnCustomer() {

        Customer customer = new Customer(dto);
        Customer savedCustomer = customerRepository.save(customer);

        assertEquals(customer, savedCustomer);
    }
}

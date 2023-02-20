package com.dangol.dangolsonnimbackend.customer.domain;

import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerTest {

    @Test
    @DisplayName("CustomerSignupRequestDTO 를 Customer와 CustomerInfo로 변환하는 테스트")
    void testCustomerSignupRequestDTOToCustomerAndCustomerInfoConversion() {
        CustomerSignupRequestDTO dto = new CustomerSignupRequestDTO();
        dto.setName("LHJ");
        dto.setEmail("dangol@example.com");
        dto.setProviderType("google");
        dto.setNickname("HOOCHU");
        dto.setProfileImageUrl("test.url");
        dto.setPhoneNumber("01012345678");
        dto.setBirth("1990-01-01");

        Customer customer = new Customer(dto);
        CustomerInfo customerInfo = new CustomerInfo(dto);

        assertEquals(customer.getName(), dto.getName());
        assertEquals(customerInfo.getNickname(), dto.getNickname());
    }
}

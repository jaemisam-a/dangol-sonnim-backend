package com.dangol.dangolsonnimbackend.customer.domain;

import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerTest {

    @Test
    @DisplayName("CustomerSignupRequestDTO 를 Customer와 CustomerInfo로 변환하는 테스트")
    void testCustomerSignupRequestDTOToCustomerAndCustomerInfoConversion() {
        CustomerSignupDTO dto = new CustomerSignupDTO();
        dto.setName("LHJ");
        dto.setNickname("HOOCHU");
        dto.setProfileImageUrl("test.url");
        dto.setPhoneNumber("01012345678");
        dto.setBirth("1990-01-01");
        dto.setMarketingAgreement(true);

        Customer customer = new Customer(dto);

        assertEquals(customer.getName(), dto.getName());
    }
}

package com.dangol.dangolsonnimbackend.boss.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import org.junit.jupiter.api.Test;

class BossTest {

    @Test
    void testBossSignupRequestDTOToBossConversion() {
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("KSM");
        dto.setBossPhoneNumber("01012345678");
        dto.setEmail("ksm@example.com");
        dto.setPassword("password");
        dto.setStoreRegisterNumber("123-456-789");
        dto.setStoreRegisterName("John's store");
        dto.setMarketingAgreement(true);

        Boss boss = new Boss(dto);

        assertEquals(dto.getName(), boss.getName());
        assertEquals(dto.getBossPhoneNumber(), boss.getBossPhoneNumber());
        assertEquals(dto.getEmail(), boss.getEmail());
        assertEquals(dto.getPassword(), boss.getPassword());
        assertEquals(dto.getStoreRegisterNumber(), boss.getStoreRegisterNumber());
        assertEquals(dto.getStoreRegisterName(), boss.getStoreRegisterName());
        assertEquals(dto.getMarketingAgreement(), boss.getMarketingAgreement());
    }

}

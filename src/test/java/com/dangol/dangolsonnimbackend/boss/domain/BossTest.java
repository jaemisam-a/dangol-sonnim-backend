package com.dangol.dangolsonnimbackend.boss.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import org.junit.jupiter.api.Test;

class BossTest {

    @Test
    void testBossSignupRequestDTOToBossConversion() {
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("KSM");
        dto.setPhoneNumber("01012345678");
        dto.setEmail("ksm@example.com");
        dto.setPassword("password");
        dto.setMarketingAgreement(true);

        Boss boss = new Boss(dto);

        assertEquals(boss.getName(), dto.getName());
    }
}

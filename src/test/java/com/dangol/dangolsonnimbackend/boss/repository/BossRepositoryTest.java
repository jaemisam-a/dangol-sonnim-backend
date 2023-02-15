package com.dangol.dangolsonnimbackend.boss.repository;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
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
class BossRepositoryTest {

    @Autowired
    private BossRepository bossRepository;

    private BossSignupRequestDTO dto;

    @BeforeEach
    void setup(){
        dto = new BossSignupRequestDTO();
        dto.setName("Test");
        dto.setPassword("password");
        dto.setEmail("test@test.com");
        dto.setBossPhoneNumber("01012345678");
        dto.setStoreRegisterNumber("1234567890");
        dto.setStoreRegisterName("Test Store");
        dto.setMarketingAgreement(true);
    }

    @Test
    void givenSignupDto_whenFindById_thenReturnBoss() {
        Boss boss = new Boss(dto);
        boss = bossRepository.save(boss);

        Optional<Boss> found = bossRepository.findById(boss.getId());
        assertTrue(found.isPresent());
        assertEquals(boss, found.get());
    }

    @Test
    void givenSignupDto_whenSaveBoss_thenReturnBoss() {
        Boss boss = new Boss(dto);
        Boss savedBoss = bossRepository.save(boss);

        assertEquals(boss, savedBoss);
    }
}

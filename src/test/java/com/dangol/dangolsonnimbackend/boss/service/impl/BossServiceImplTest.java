package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BossServiceImplTest {

    @Autowired
    private BossRepository bossRepository;

    @Autowired
    private BossQueryRepository bossQueryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BossServiceImpl bossService;

    private BossSignupRequestDTO validDto;

    @BeforeEach
    public void setup() {
        validDto = new BossSignupRequestDTO();

        validDto.setName("Test");
        validDto.setPassword("password");
        validDto.setEmail("test@test.com");
        validDto.setBossPhoneNumber("01012345678");
        validDto.setStoreRegisterNumber("1234567890");
        validDto.setStoreRegisterName("Test Store");
        validDto.setMarketingAgreement(true);

    }

    @Test
    public void givenValidDto_whenSignup_thenSaveBoss() {
        // given
        // validDto set up in beforeEach method

        // when
        bossService.signup(validDto);

        // then
        Boss boss = bossQueryRepository.findByEmail(validDto.getEmail());
        assertNotNull(boss);
        assertBossEqualsDto(boss, validDto);
    }

    @Test
    public void givenSignup_whenDuplicateSrn_thenThrowException() {
        // given
        bossRepository.save(new Boss(validDto));

        // when, then
        assertThrows(RuntimeException.class, () -> bossService.signup(validDto));
    }

    @Test
    public void givenSignup_whenDuplicateEmail_thenThrowException() {
        // given
        BossSignupRequestDTO dtoWithDuplicateEmail = new BossSignupRequestDTO();

        dtoWithDuplicateEmail.setName("Test");
        dtoWithDuplicateEmail.setPassword("password");
        dtoWithDuplicateEmail.setEmail(validDto.getEmail());
        dtoWithDuplicateEmail.setBossPhoneNumber("010123");
        dtoWithDuplicateEmail.setStoreRegisterNumber("12345");
        dtoWithDuplicateEmail.setStoreRegisterName("Test Store");
        dtoWithDuplicateEmail.setMarketingAgreement(true);
        bossRepository.save(new Boss(dtoWithDuplicateEmail));

        // when, then
        assertThrows(RuntimeException.class, () -> bossService.signup(dtoWithDuplicateEmail));
    }

    private void assertBossEqualsDto(Boss boss, BossSignupRequestDTO dto) {
        assertEquals(dto.getEmail(), boss.getEmail());
        assertTrue(passwordEncoder.matches("password", boss.getPassword()));
        assertEquals(dto.getStoreRegisterName(), boss.getStoreRegisterName());
    }
}

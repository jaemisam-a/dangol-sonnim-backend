package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BossServiceImplTest {

    @Mock
    private BossRepository bossRepository;
    @Mock
    private BossQueryRepository bossQueryRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private BossServiceImpl bossServiceImpl;

    private BossSignupRequestDTO dto;

    @BeforeEach
    void setUp() {
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
    void givenExistingStoreRegisterName_whenSignup_thenThrowException() {
        when(bossQueryRepository.existsBySrn(anyString())).thenReturn(true);
        assertThrows(RuntimeException.class, () -> bossServiceImpl.signup(dto));
    }

    @Test
    void givenExistingBossPhoneNumber_whenSignup_thenThrowException() {
        when(bossQueryRepository.existsBySrn(anyString())).thenReturn(false);
        when(bossQueryRepository.existsByBpn(anyString())).thenReturn(true);
        assertThrows(RuntimeException.class, () -> bossServiceImpl.signup(dto));
    }

    @Test
    void givenExistingEmail_whenSignup_thenThrowException() {
        when(bossQueryRepository.existsBySrn(anyString())).thenReturn(false);
        when(bossQueryRepository.existsByBpn(anyString())).thenReturn(false);
        when(bossQueryRepository.existsByEmail(anyString())).thenReturn(true);
        assertThrows(RuntimeException.class, () -> bossServiceImpl.signup(dto));
    }

    @Test
    void givenNotExistingSrnBpnEmail_whenSignup_thenSuccess() {
        when(bossQueryRepository.existsBySrn(anyString())).thenReturn(false);
        when(bossQueryRepository.existsByBpn(anyString())).thenReturn(false);
        when(bossQueryRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        bossServiceImpl.signup(dto);
        verify(bossRepository).save(any(Boss.class));
    }
}
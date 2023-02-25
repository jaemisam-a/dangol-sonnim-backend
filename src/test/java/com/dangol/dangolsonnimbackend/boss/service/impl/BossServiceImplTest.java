package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninReqeustDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossUpdateRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import org.junit.jupiter.api.Assertions;
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
        validDto.setPhoneNumber("01012345678");
        validDto.setMarketingAgreement(true);
    }

    @Test
    public void givenValidDto_whenSignup_thenSaveBoss() {
        // given

        // when
        bossService.signup(validDto);

        // then
        Boss boss = bossQueryRepository.findByEmail(validDto.getEmail());
        assertNotNull(boss);
        assertBossEqualsDto(boss, validDto);
    }

    @Test
    public void givenSignup_whenDuplicatePhoneNumber_thenThrowException() {
        // given
        BossSignupRequestDTO dtoWithDuplicateEmail = new BossSignupRequestDTO();

        dtoWithDuplicateEmail.setName("Test");
        dtoWithDuplicateEmail.setPassword("password");
        dtoWithDuplicateEmail.setEmail("Test@Email.com");
        dtoWithDuplicateEmail.setPhoneNumber(validDto.getPhoneNumber());
        dtoWithDuplicateEmail.setMarketingAgreement(true);
        bossRepository.save(new Boss(dtoWithDuplicateEmail));

        // when, then
        assertThrows(RuntimeException.class, () -> bossService.signup(dtoWithDuplicateEmail));
    }

    @Test
    public void givenSignup_whenDuplicateEmail_thenThrowException() {
        // given
        BossSignupRequestDTO dtoWithDuplicateEmail = new BossSignupRequestDTO();

        dtoWithDuplicateEmail.setName("Test");
        dtoWithDuplicateEmail.setPassword("password");
        dtoWithDuplicateEmail.setEmail(validDto.getEmail());
        dtoWithDuplicateEmail.setPhoneNumber("010123");
        dtoWithDuplicateEmail.setMarketingAgreement(true);
        bossRepository.save(new Boss(dtoWithDuplicateEmail));

        // when, then
        assertThrows(RuntimeException.class, () -> bossService.signup(dtoWithDuplicateEmail));
    }

    private void assertBossEqualsDto(Boss boss, BossSignupRequestDTO dto) {
        assertEquals(dto.getEmail(), boss.getEmail());
        assertTrue(passwordEncoder.matches("password", boss.getPassword()));
    }

    @Test
    void givenValidEmail_whenFindByEmail_thenReturnBoss() {
        // given
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("Test Boss");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setPhoneNumber("01012345678");
        dto.setMarketingAgreement(true);

        bossService.signup(dto);

        // when
        Boss boss = bossService.findByEmail(dto.getEmail());

        // then
        assertNotNull(boss);
        assertEquals(dto.getName(), boss.getName());
    }

    @Test
    void givenInvalidEmail_whenFindByEmail_thenThrowException() {
        // given
        String email = "invalid@example.com";

        // when then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> bossService.findByEmail(email));
        assertEquals(ErrorCodeMessage.BOSS_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void givenValidCredentials_whenGetByCredentials_thenReturnBossSigninResponseDTO() {
        // given
        bossService.signup(validDto);

        BossSigninReqeustDTO credentials = new BossSigninReqeustDTO("test@test.com", "password");

        // when
        BossSigninResponseDTO response = bossService.getByCredentials(credentials);

        // then
        assertNotNull(response.getAccessToken());
    }

    @Test
    void givenInvalidEmail_whenGetByCredentials_thenThrowBadRequestException() {
        // given
        BossSigninReqeustDTO credentials = new BossSigninReqeustDTO("test@test.com", "password");

        // when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> bossService.getByCredentials(credentials));

        // then
        assertEquals(ErrorCodeMessage.BOSS_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void givenValidEmail_whenWithDraw_thenReturnTrue() {

        // given
        bossService.signup(validDto);

        // when
        bossService.withdraw(validDto.getEmail());

        // then
        Assertions.assertNull(bossQueryRepository.findByEmail("test@example.com"));
    }

    @Test
    void givenUpdateDTO_whenUpdate_thenBossUpdated() {

        // given
        bossService.signup(validDto);
        BossUpdateRequestDTO requestDTO = new BossUpdateRequestDTO("01012345678", null);

        // when
        bossService.update(validDto.getEmail(), requestDTO);

        // then
        Boss savedBoss = bossQueryRepository.findByEmail("test@test.com");
        Assertions.assertNotNull(savedBoss.getMarketingAgreement());
        Assertions.assertEquals(savedBoss.getPhoneNumber(), "01012345678");
    }
}

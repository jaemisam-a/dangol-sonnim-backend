package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.*;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.config.jwt.TokenProvider;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BossControllerTest {

    @Autowired
    private BossService bossService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenSignupDto_whenSignup_thenCreateNewBoss() throws Exception {
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("TestBoss");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setPhoneNumber("01012345678");
        dto.setMarketingAgreement(true);

        mockMvc.perform(post("/api/v1/boss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Boss boss = bossService.findByEmail(dto.getEmail());
        assertNotNull(boss);
        assertEquals(dto.getName(), boss.getName());
    }

    @Test
    void givenValidCredentials_whenAuthenticate_thenSucceed() throws Exception {
        // given
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("Test Boss");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setPhoneNumber("01012345678");
        dto.setMarketingAgreement(true);
        bossService.signup(dto);

        BossSigninReqeustDTO requestDTO = new BossSigninReqeustDTO("test@example.com", "password");

        // when
        MvcResult result = mockMvc.perform(
                        post("/api/v1/boss/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDTO))
                                .with(csrf())
                ).andExpect(status().isOk())
                .andReturn();

        // then
        String contentAsString = result.getResponse().getContentAsString();
        BossSigninResponseDTO responseDTO = new ObjectMapper().readValue(contentAsString, BossSigninResponseDTO.class);
        String token = responseDTO.getAccessToken();
        assertNotNull(token);
        String getEmail = tokenProvider.getEmailFromToken(token);
        assertEquals("test@example.com", getEmail);
    }

    @Test
    void givenInvalidCredentials_whenAuthenticate_thenFail() throws Exception {
        // given
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("Test Boss");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setPhoneNumber("01012345678");
        dto.setMarketingAgreement(true);
        bossService.signup(dto);

        BossSigninReqeustDTO requestDTO = new BossSigninReqeustDTO("test@example.com", "invalid-password");

        // when
        mockMvc.perform(
                        post("/api/v1/boss/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDTO))
                                .with(csrf())
                )
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenBossEmail_whenWithdraw_thenDeleteBoss() throws Exception {
        // given
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("Test Boss");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setPhoneNumber("01012345678");
        dto.setMarketingAgreement(true);
        bossService.signup(dto);

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        mockMvc.perform(delete("/api/v1/boss")
                    .with(csrf()))
                .andExpect(status().isNoContent());

        // then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> bossService.findByEmail(dto.getEmail()));
        assertEquals(ErrorCodeMessage.BOSS_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void givenBossEmail_whenGetBoss_thenReturnBoss() throws Exception {
        // given
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("Test Boss");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setPhoneNumber("01012345678");
        dto.setMarketingAgreement(true);
        bossService.signup(dto);

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/boss")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        BossResponseDTO responseDTO = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), BossResponseDTO.class);
        assertEquals(dto.getName(), responseDTO.getName());
        assertNotNull(responseDTO.getCreatedAt());
    }

    @Test
    void givenBossEmail_whenUpdate_thenReturnBoss() throws Exception {
        // given
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("Test Boss");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setPhoneNumber("01012345678");
        dto.setMarketingAgreement(true);
        bossService.signup(dto);

        BossUpdateRequestDTO requestDTO = new BossUpdateRequestDTO(null, false);

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        MvcResult mvcResult = mockMvc.perform(
                        patch("/api/v1/boss")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDTO))
                                .with(csrf())
                )
                // then
                .andExpect(status().isOk())
                .andReturn();

        BossResponseDTO responseDTO = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), BossResponseDTO.class);
        assertEquals(dto.getName(), responseDTO.getName());
        assertEquals(dto.getPhoneNumber(), responseDTO.getPhoneNumber());
        assertEquals(responseDTO.getMarketingAgreement(), false);
    }
}

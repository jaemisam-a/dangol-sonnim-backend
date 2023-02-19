package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninReqeustDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.config.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        dto.setName("Test Boss");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setPhoneNumber("01012345678");
        dto.setMarketingAgreement(true);

        mockMvc.perform(post("/api/v1/boss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
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
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
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
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                // then
                .andExpect(status().isBadRequest());
    }
}

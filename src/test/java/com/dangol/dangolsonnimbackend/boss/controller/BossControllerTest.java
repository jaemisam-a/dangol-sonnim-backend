package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BossControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BossQueryRepository bossQueryRepository;

    @Test
    void givenSignupDto_whenSignup_thenCreateNewBoss() throws Exception {
        BossSignupRequestDTO dto = new BossSignupRequestDTO();
        dto.setName("Test Boss");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setStoreRegisterName("Test Store");
        dto.setStoreRegisterNumber("1234567890");
        dto.setBossPhoneNumber("01012345678");
        dto.setMarketingAgreement(true);

        mockMvc.perform(post("/api/boss/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Boss boss = bossQueryRepository.findByEmail(dto.getEmail());
        assertNotNull(boss);
        assertEquals(dto.getName(), boss.getName());
    }

}

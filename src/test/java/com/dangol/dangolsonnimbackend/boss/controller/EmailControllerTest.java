package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.dto.AuthCodeRequestDTO;
import com.dangol.dangolsonnimbackend.boss.dto.AuthCodeResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenEmail_whenSendAuthCode_thenSuccess() throws Exception {

        // given
        String email = "test@example.com";
        AuthCodeRequestDTO requestDTO = new AuthCodeRequestDTO();
        requestDTO.setEmail(email);

        // when 최대 허용 요청 횟수 이내의 요청인 경우
        for (int i = 0; i < 5; i++) {
            MvcResult result = mockMvc.perform(post("/api/v1/email/send-auth-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
                        .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String contentAsString = result.getResponse().getContentAsString();
            AuthCodeResponseDTO responseDTO = new ObjectMapper().readValue(contentAsString, AuthCodeResponseDTO.class);
            assertNotNull(responseDTO.getAuthCode());
        }
        // when 최대 허용 요청 횟수를 초과한 경우
        mockMvc.perform(post("/api/v1/email/send-auth-code")
                        .with(csrf())
                        .param("email", email))
                // then
                .andExpect(status().isBadRequest());
    }
}

package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.dto.request.AuthKeyRequestDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.IsValidAuthCodeRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(RestDocumentationExtension.class) // RestDocumentation 기능을 사용하기 위한 어노테이션
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EmailService emailService;

    private static final String BASE_URL = "/api/v1/email";
    private static final String EMAIL = "test@example.com";
    private static final String AUTH_CODE = "ABCDEFGH";

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation) // Spring REST Docs 의 설정
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    void givenEmail_whenSendAuthCode_thenSuccess() throws Exception {

        // given
        AuthKeyRequestDTO requestDTO = new AuthKeyRequestDTO(EMAIL);

        mockMvc.perform(post(BASE_URL + "/send-auth-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDTO))
                .with(csrf())
            )
            .andExpect(status().isNoContent())
            .andDo(document("boss/emailSend",
                    requestFields(
                            fieldWithPath("email").type(JsonFieldType.STRING).description("사장님 이메일 주소")
                    )
            ));
    }

    @Test
    void givenIsValidAuthCodeRequestDTO_whenIsValidAuthCode_thenReturnTrue() throws Exception {
        // given
        emailService.create(EMAIL, AUTH_CODE);
        IsValidAuthCodeRequestDTO requestDTO = new IsValidAuthCodeRequestDTO(EMAIL, AUTH_CODE);

        mockMvc.perform(post(BASE_URL + "/valid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(jsonPath("$.isValid").value(true))
                .andDo(document("boss/emailValid",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("사장님 이메일 주소"),
                                fieldWithPath("authCode").type(JsonFieldType.STRING).description("인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("isValid").type(JsonFieldType.BOOLEAN).description("인증 코드 일치 여부")
                        )
                ));

    }
}

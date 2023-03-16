package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.dto.*;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.config.jwt.TokenProvider;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
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
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(RestDocumentationExtension.class) // RestDocumentation 기능을 사용하기 위한 어노테이션
class BossControllerTest {

    @Autowired
    private BossService bossService;
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private BossSignupRequestDTO dto;

    private static final String BASE_URL = "/api/v1/boss";
    private static final String BOSS_TEST_NAME = "GilDong";
    private static final String BOSS_TEST_EMAIL = "test@example.com";
    private static final String BOSS_TEST_PASSWORD = "password";
    private static final String BOSS_TEST_PHONE_NUMBER = "01012345678";
    private static final Boolean BOSS_TEST_MARKETING_AGREEMENT = true;

    private final FieldDescriptor[] signupRequestJsonField = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("사장님 이름"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("사장님 이메일 주소"),
            fieldWithPath("password").type(JsonFieldType.STRING).description("사장님 패스워드"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("사장님 휴대폰번호"),
            fieldWithPath("marketingAgreement").type(JsonFieldType.BOOLEAN).description("마케팅 수신 동의 여부")
    };

    private final FieldDescriptor[] signupResponseJsonField = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("사장님 이름"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("사장님 이메일 주소"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("사장님 휴대폰번호"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사장님 생성 날짜"),
            fieldWithPath("marketingAgreement").type(JsonFieldType.BOOLEAN).description("마케팅 수신 동의 여부"),
            fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("Self link"),
            fieldWithPath("_links.authenticate.href").type(JsonFieldType.STRING).description("authenticate link")
    };

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();

        dto = new BossSignupRequestDTO();
        dto.setName(BOSS_TEST_NAME);
        dto.setEmail(BOSS_TEST_EMAIL);
        dto.setPassword(BOSS_TEST_PASSWORD);
        dto.setPhoneNumber(BOSS_TEST_PHONE_NUMBER);
        dto.setMarketingAgreement(BOSS_TEST_MARKETING_AGREEMENT);
    }

    @Test
    void givenSignupDto_whenSignup_thenCreateNewBoss() throws Exception {
        // given when
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(dto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(BOSS_TEST_NAME))
                .andExpect(jsonPath("$.email").value(BOSS_TEST_EMAIL))
                .andExpect(jsonPath("$.phoneNumber").value(BOSS_TEST_PHONE_NUMBER))
                .andExpect(jsonPath("$.marketingAgreement").value(BOSS_TEST_MARKETING_AGREEMENT))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.authenticate").exists())
                .andDo(document("boss/signup",
                        requestFields(signupRequestJsonField),
                        responseFields(signupResponseJsonField)
                ));
    }

    @Test
    void givenValidCredentials_whenAuthenticate_thenSucceed() throws Exception {
        // given
        bossService.signup(dto);

        BossSigninReqeustDTO requestDTO = new BossSigninReqeustDTO("test@example.com", "password");

        // when
        MvcResult result = mockMvc.perform(
                        post(BASE_URL + "/signin")
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
        assertEquals(BOSS_TEST_EMAIL, getEmail);
    }

    @Test
    void givenInvalidCredentials_whenAuthenticate_thenFail() throws Exception {
        // given
        bossService.signup(dto);
        BossSigninReqeustDTO requestDTO = new BossSigninReqeustDTO("test@example.com", "invalid-password");

        // when
        mockMvc.perform(
                        post(BASE_URL + "/signin")
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
        bossService.signup(dto);

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        mockMvc.perform(delete(BASE_URL)
                    .with(csrf()))
                .andExpect(status().isNoContent());

        // then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> bossService.findByEmail("invalid"));
        assertEquals(ErrorCodeMessage.BOSS_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void givenBossEmail_whenGetBoss_thenReturnBoss() throws Exception {
        // given
        bossService.signup(dto);

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL)
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
        bossService.signup(dto);

        BossUpdateRequestDTO requestDTO = new BossUpdateRequestDTO(null, false);

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        MvcResult mvcResult = mockMvc.perform(
                        patch(BASE_URL)
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
        assertEquals(false, responseDTO.getMarketingAgreement());
    }

    @Test
    void givenBossPhoneNumber_whenFindByEmail_thenReturnBoss() throws Exception {
        // given
        bossService.signup(dto);

        BossFindEmailReqeustDTO requestDTO = new BossFindEmailReqeustDTO();
        requestDTO.setPhoneNumber(dto.getPhoneNumber());

        // when
        MvcResult mvcResult = mockMvc.perform(
                        post(BASE_URL + "/find-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDTO))
                                .with(csrf())
                )
                // then
                .andExpect(status().isOk())
                .andReturn();

        BossFindEmailResponseDTO responseDTO = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), BossFindEmailResponseDTO.class);
        assertEquals(dto.getEmail(), responseDTO.getEmail());
    }
}

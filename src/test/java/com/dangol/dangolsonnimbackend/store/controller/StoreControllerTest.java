package com.dangol.dangolsonnimbackend.store.controller;

import com.dangol.dangolsonnimbackend.boss.dto.request.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.config.jwt.TokenProvider;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.dto.BusinessHourRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.CategoryRepository;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage.STORE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(RestDocumentationExtension.class) // RestDocumentation 기능을 사용하기 위한 어노테이션
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private StoreSignupRequestDTO dto;

    @Autowired
    private WebApplicationContext context;
    private static final String BOSS_TEST_NAME = "GilDong";
    private static final String BOSS_TEST_EMAIL = "test@example.com";
    private static final String BOSS_TEST_PASSWORD = "password";
    private static final String BOSS_TEST_PHONE_NUMBER = "01012345678";
    private static final Boolean BOSS_TEST_MARKETING_AGREEMENT = true;
    private String accessToken;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private StoreService storeService;
    @Autowired
    private BossService bossService;
    @Autowired
    private CategoryRepository categoryRepository;

    FieldDescriptor[] signUpRequestJsonField = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("가게 이름"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("가게 전화번호"),
            fieldWithPath("newAddress").type(JsonFieldType.STRING).description("가게 주소"),
            fieldWithPath("sido").type(JsonFieldType.STRING).description("가게 주소 (시/도)"),
            fieldWithPath("sigungu").type(JsonFieldType.STRING).description("가게 주소 (시/군/구)"),
            fieldWithPath("bname1").type(JsonFieldType.STRING).optional().description("가게 주소 (읍/면)"),
            fieldWithPath("bname2").type(JsonFieldType.STRING).optional().description("가게 주소 (동/리)"),
            fieldWithPath("detailedAddress").type(JsonFieldType.STRING).optional().description("가게 상세주소"),
            fieldWithPath("comments").type(JsonFieldType.STRING).description("가게 한줄평"),
            fieldWithPath("categoryType").type(JsonFieldType.VARIES).description("카테고리 정보"),
            fieldWithPath("registerNumber").type(JsonFieldType.STRING).description("가게 사업자번호"),
            fieldWithPath("registerName").type(JsonFieldType.STRING).description("가게 사업자명"),
            fieldWithPath("tags").type(JsonFieldType.ARRAY).description("가게 태그"),
            fieldWithPath("businessHours[].weeks").type(JsonFieldType.STRING).description("영업 요일"),
            fieldWithPath("businessHours[].hours").type(JsonFieldType.STRING).description("영업 시간")
    };

    FieldDescriptor[] findResponseJsonField = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("가게 아이디"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("가게 이름"),
//            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("가게 전화번호"),
            fieldWithPath("newAddress").type(JsonFieldType.STRING).description("가게 주소"),
            fieldWithPath("comments").type(JsonFieldType.STRING).description("가게 한줄평"),
            fieldWithPath("sido").type(JsonFieldType.STRING).description("가게 주소 (시/도)"),
            fieldWithPath("sigungu").type(JsonFieldType.STRING).description("가게 주소 (시/군/구)"),
            fieldWithPath("bname1").type(JsonFieldType.STRING).description("가게 주소 (읍/면)"),
            fieldWithPath("bname2").type(JsonFieldType.STRING).description("가게 주소 (동/리)"),
            fieldWithPath("detailedAddress").type(JsonFieldType.STRING).description("가게 상세주소"),
            fieldWithPath("categoryType").type(JsonFieldType.VARIES).description("카테고리 정보"),
            fieldWithPath("registerNumber").type(JsonFieldType.STRING).description("가게 사업자번호"),
            fieldWithPath("registerName").type(JsonFieldType.STRING).description("가게 사업자명"),
            fieldWithPath("tags").type(JsonFieldType.ARRAY).description("가게 태그"),
            fieldWithPath("businessHours[].weeks").type(JsonFieldType.STRING).description("영업 요일"),
            fieldWithPath("businessHours[].hours").type(JsonFieldType.STRING).description("영업 시간")
    };

    FieldDescriptor[] updateRequestJsonField = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).optional().description("가게 이름"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).optional().description("가게 전화번호"),
            fieldWithPath("newAddress").type(JsonFieldType.STRING).optional().description("가게 주소"),
            fieldWithPath("sido").type(JsonFieldType.STRING).optional().description("가게 주소 (시/도)"),
            fieldWithPath("sigungu").type(JsonFieldType.STRING).optional().description("가게 주소 (시/군/구)"),
            fieldWithPath("bname1").type(JsonFieldType.STRING).optional().description("가게 주소 (읍/면)"),
            fieldWithPath("bname2").type(JsonFieldType.STRING).optional().description("가게 주소 (동/리)"),
            fieldWithPath("detailedAddress").type(JsonFieldType.STRING).optional().description("가게 상세주소"),
            fieldWithPath("comments").type(JsonFieldType.STRING).optional().description("가게 한줄평"),
            fieldWithPath("categoryType").type(JsonFieldType.VARIES).description("카테고리 정보"),
            fieldWithPath("registerNumber").type(JsonFieldType.STRING).description("가게 사업자번호"),
            fieldWithPath("registerName").type(JsonFieldType.STRING).optional().description("가게 사업자명"),
            fieldWithPath("tags").type(JsonFieldType.ARRAY).description("가게 태그"),
            fieldWithPath("businessHours[].weeks").type(JsonFieldType.STRING).description("영업 요일"),
            fieldWithPath("businessHours[].hours").type(JsonFieldType.STRING).description("영업 시간")
    };

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();

        dto = StoreSignupRequestDTO.builder()
                .name("단골손님" + new Random().nextInt())
                .phoneNumber("01012345678")
                .newAddress("서울특별시 서초구 단골로 130")
                .sido("서울특별시")
                .sigungu("서초구")
                .bname1("단골동")
                .bname2("")
                .detailedAddress("")
                .comments("단골손님 가게로 좋아요.")
                .categoryType(CategoryType.KOREAN)
                .registerNumber("1234567890")
                .registerName("단골손님")
                .tags(List.of("태그1", "태그2"))
                .businessHours(List.of(new BusinessHourRequestDTO("월~수", "10:00~12:00"),
                        new BusinessHourRequestDTO("토,일", "10:00~12:00")))
                .build();

        BossSignupRequestDTO bossSignupRequestDTO = new BossSignupRequestDTO();
        bossSignupRequestDTO.setName(BOSS_TEST_NAME);
        bossSignupRequestDTO.setEmail(BOSS_TEST_EMAIL);
        bossSignupRequestDTO.setPassword(BOSS_TEST_PASSWORD);
        bossSignupRequestDTO.setPhoneNumber(BOSS_TEST_PHONE_NUMBER);
        bossSignupRequestDTO.setMarketingAgreement(BOSS_TEST_MARKETING_AGREEMENT);
        bossService.signup(bossSignupRequestDTO);

        accessToken = tokenProvider.generateAccessToken(bossSignupRequestDTO.getEmail());
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                tokenProvider.getEmailFromToken(accessToken), null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("새로운 가게 정보 생성을 요청하면 정상적으로 생성이 된다.")
    void givenSignupDto_whenSignup_thenCreateNewStore() throws Exception {
        mockMvc.perform(post("/api/v1/store/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.newAddress").value(dto.getNewAddress()))
                .andExpect(jsonPath("$.comments").value(dto.getComments()))
                .andExpect(jsonPath("$.sigungu").value(dto.getSigungu()))
                .andExpect(jsonPath("$.bname1").value(dto.getBname1()))
                .andExpect(jsonPath("$.bname2").value(dto.getBname2()))
                .andExpect(jsonPath("$.detailedAddress").value(dto.getDetailedAddress()))
                .andExpect(jsonPath("$.categoryType").value(dto.getCategoryType().toString()))
                .andExpect(jsonPath("$.tags[0]").exists())
                .andExpect(jsonPath("$.tags[1]").exists())
                .andExpect(jsonPath("$.businessHours[0].weeks").value(dto.getBusinessHours().get(0).getWeeks()))
                .andExpect(jsonPath("$.businessHours[0].hours").value(dto.getBusinessHours().get(0).getHours()))
                .andExpect(jsonPath("$.businessHours[1].weeks").value(dto.getBusinessHours().get(1).getWeeks()))
                .andExpect(jsonPath("$.businessHours[1].hours").value(dto.getBusinessHours().get(1).getHours()))
                .andDo(document("store/create",
                        requestHeaders(headerWithName("Authorization").description("Access 토큰 정보")),
                        requestFields(signUpRequestJsonField)
                ));

        // 재등록 시에 이미 존재하는 사업자 번호로 반환
        assertThrows(BadRequestException.class, () -> storeService.create(dto, BOSS_TEST_EMAIL));
    }

    @Test
    @Transactional
    @DisplayName("특정 가게 아이디 값을 통해 가게를 검색할 수 있다.")
    void givenStoreId_whenFindById_thenReturnStore() throws Exception {
        Long id = storeService.create(dto, BOSS_TEST_EMAIL).getId();

        mockMvc.perform(get("/api/v1/store/find?id="+ id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.newAddress").value(dto.getNewAddress()))
                .andExpect(jsonPath("$.comments").value(dto.getComments()))
                .andExpect(jsonPath("$.sigungu").value(dto.getSigungu()))
                .andExpect(jsonPath("$.bname1").value(dto.getBname1()))
                .andExpect(jsonPath("$.bname2").value(dto.getBname2()))
                .andExpect(jsonPath("$.detailedAddress").value(dto.getDetailedAddress()))
                .andExpect(jsonPath("$.categoryType").value(dto.getCategoryType().toString()))
                .andExpect(jsonPath("$.tags[0]").exists())
                .andExpect(jsonPath("$.businessHours[0].weeks").value(dto.getBusinessHours().get(0).getWeeks()))
                .andExpect(jsonPath("$.businessHours[0].hours").value(dto.getBusinessHours().get(0).getHours()))
                .andExpect(jsonPath("$.businessHours[1].weeks").value(dto.getBusinessHours().get(1).getWeeks()))
                .andExpect(jsonPath("$.businessHours[1].hours").value(dto.getBusinessHours().get(1).getHours()))
                .andDo(document("store/find",
                        requestParameters(
                                parameterWithName("id").description("가게 아이디"),
                                parameterWithName("_csrf").description("CSRF 토큰 정보")),
                        responseFields(findResponseJsonField)

                ));
    }

    @Test
    @Transactional
    @DisplayName("특정 가게 정보를 업데이트하여 정보를 수정할 수 있다.")
    void givenStore_whenUpdate_thenUpdateStore() throws Exception {
        Category category = new Category();
        category.setCategoryType(CategoryType.CHINESE);
        categoryRepository.save(category);

        String registerNumber = storeService.create(dto, BOSS_TEST_EMAIL).getRegisterNumber();

        StoreUpdateDTO updateDTO = new StoreUpdateDTO(registerNumber);

        updateDTO.setName(Optional.of("단골손님" + new Random().nextInt()));
        updateDTO.setSido(Optional.of("경기도"));
        updateDTO.setCategoryType(Optional.of(CategoryType.CHINESE));
        updateDTO.setTags(List.of("변경 태그1"));
        updateDTO.setBusinessHours(List.of(new BusinessHourRequestDTO("수~목", "11:00~12:00")));

        mockMvc.perform(patch("/api/v1/store/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDTO.getName().get()))
                .andExpect(jsonPath("$.sido").value(updateDTO.getSido().get()))
                .andExpect(jsonPath("$.categoryType").value(updateDTO.getCategoryType().get().toString()))
                .andExpect(jsonPath("$.tags[0]").exists())
                .andExpect(jsonPath("$.businessHours[0].weeks").value(updateDTO.getBusinessHours().get(0).getWeeks()))
                .andExpect(jsonPath("$.businessHours[0].hours").value(updateDTO.getBusinessHours().get(0).getHours()))
                .andDo(document("store/update",
                        requestFields(updateRequestJsonField)
                ));
    }

    @Test
    @Transactional
    @DisplayName("존재하지 않은 가게 정보로 업데이트를 요청하면 Bad Request를 반환받는다.")
    void givenNonExistedStore_whenUpdate_thenThrowException() throws Exception {
        StoreUpdateDTO updateDTO = new StoreUpdateDTO("None");

        mockMvc.perform(patch("/api/v1/store/update")
                        .contentType(MediaType.APPLICATION_JSON)
                           .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(STORE_NOT_FOUND.getMessage()));
    }

    @Test
    @Transactional
    @DisplayName("로그인된 사장님 유저의 가게를 조회한다.")
    void givenBoss_whenFindMyStore_thenReturnStore() throws Exception {
        // given
        storeService.create(dto, BOSS_TEST_EMAIL);

        // when
        mockMvc.perform(get("/api/v1/store/my-store")
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value(dto.getName()))
                .andExpect(jsonPath("$[0].newAddress").value(dto.getNewAddress()))
                .andExpect(jsonPath("$[0].comments").value(dto.getComments()))
                .andExpect(jsonPath("$[0].sigungu").value(dto.getSigungu()))
                .andExpect(jsonPath("$[0].bname1").value(dto.getBname1()))
                .andExpect(jsonPath("$[0].bname2").value(dto.getBname2()))
                .andExpect(jsonPath("$[0].detailedAddress").value(dto.getDetailedAddress()))
                .andExpect(jsonPath("$[0].categoryType").value(dto.getCategoryType().toString()))
                .andExpect(jsonPath("$[0].tags[0]").exists())
                .andExpect(jsonPath("$[0].businessHours[0].weeks").value(dto.getBusinessHours().get(0).getWeeks()))
                .andExpect(jsonPath("$[0].businessHours[0].hours").value(dto.getBusinessHours().get(0).getHours()))
                .andExpect(jsonPath("$[0].businessHours[1].weeks").value(dto.getBusinessHours().get(1).getWeeks()))
                .andExpect(jsonPath("$[0].businessHours[1].hours").value(dto.getBusinessHours().get(1).getHours()))
                .andDo(document("store/my-store",
                        requestHeaders(headerWithName("Authorization").description("Access 토큰 정보")),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("가게 아이디"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("[].newAddress").type(JsonFieldType.STRING).description("가게 주소"),
                                fieldWithPath("[].comments").type(JsonFieldType.STRING).description("가게 한줄평"),
                                fieldWithPath("[].sido").type(JsonFieldType.STRING).description("가게 주소 (시/도)"),
                                fieldWithPath("[].sigungu").type(JsonFieldType.STRING).description("가게 주소 (시/군/구)"),
                                fieldWithPath("[].bname1").type(JsonFieldType.STRING).description("가게 주소 (읍/면)"),
                                fieldWithPath("[].bname2").type(JsonFieldType.STRING).description("가게 주소 (동/리)"),
                                fieldWithPath("[].detailedAddress").type(JsonFieldType.STRING).description("가게 상세주소"),
                                fieldWithPath("[].categoryType").type(JsonFieldType.VARIES).description("카테고리 정보"),
                                fieldWithPath("[].registerNumber").type(JsonFieldType.STRING).description("가게 사업자번호"),
                                fieldWithPath("[].registerName").type(JsonFieldType.STRING).description("가게 사업자명"),
                                fieldWithPath("[].tags").type(JsonFieldType.ARRAY).description("가게 태그"),
                                fieldWithPath("[].businessHours[].weeks").type(JsonFieldType.STRING).description("영업 요일"),
                                fieldWithPath("[].businessHours[].hours").type(JsonFieldType.STRING).description("영업 시간")
                        )
                ));
    }
}

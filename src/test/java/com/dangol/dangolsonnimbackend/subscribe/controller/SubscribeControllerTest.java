package com.dangol.dangolsonnimbackend.subscribe.controller;

import com.dangol.dangolsonnimbackend.boss.dto.request.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.CategoryRepository;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import com.dangol.dangolsonnimbackend.subscribe.dto.BenefitDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
import com.dangol.dangolsonnimbackend.subscribe.service.SubscribeService;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType.COUNT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(RestDocumentationExtension.class) // RestDocumentation 기능을 사용하기 위한 어노테이션
class SubscribeControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;
    private static final String BASE_URL = "/api/v1/subscribe";
    @Autowired
    private StoreService storeService;
    @Autowired
    private SubscribeService subscribeService;
    private SubscribeRequestDTO subscribeRequestDTO;
    private static final String BOSS_TEST_NAME = "GilDong";
    private static final String BOSS_TEST_EMAIL = "test@example.com";
    private static final String BOSS_TEST_PASSWORD = "password";
    private static final String BOSS_TEST_PHONE_NUMBER = "01012345678";
    private static final Boolean BOSS_TEST_MARKETING_AGREEMENT = true;
    @Autowired
    private BossService bossService;

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

        StoreSignupRequestDTO signupRequestDTO = StoreSignupRequestDTO.builder()
                .name("단골손님" + new Random().nextInt())
                .phoneNumber("01012345678")
                .newAddress("서울특별시 서초구 단골로 130")
                .sido("서울특별시")
                .sigungu("서초구")
                .bname1("단골동")
                .bname2("")
                .detailedAddress("")
                .comments("단골손님 가게로 좋아요.")
                .officeHours("08:00~10:00")
                .registerNumber("1234567890")
                .registerName("단골손님")
                .categoryType(CategoryType.KOREAN)
                .tags(List.of("태그1", "태그2"))
                .build();

        List<BenefitDTO> benefitDTOList = List.of(
                new BenefitDTO("Benefit 1"),
                new BenefitDTO("Benefit 2"),
                new BenefitDTO("Benefit 3")
        );

        BossSignupRequestDTO bossSignupRequestDTO = new BossSignupRequestDTO();
        bossSignupRequestDTO.setName(BOSS_TEST_NAME);
        bossSignupRequestDTO.setEmail(BOSS_TEST_EMAIL);
        bossSignupRequestDTO.setPassword(BOSS_TEST_PASSWORD);
        bossSignupRequestDTO.setPhoneNumber(BOSS_TEST_PHONE_NUMBER);
        bossSignupRequestDTO.setMarketingAgreement(BOSS_TEST_MARKETING_AGREEMENT);
        bossService.signup(bossSignupRequestDTO);

        Long storeId = storeService.create(signupRequestDTO, BOSS_TEST_EMAIL).getId();
        subscribeRequestDTO = SubscribeRequestDTO.builder()
                .isTop(true)
                .useCount(5)
                .type(COUNT)
                .storeId(storeId)
                .price(BigDecimal.valueOf(12000))
                .intro("INTRO TEST")
                .name("NAME TEST")
                .benefits(benefitDTOList)
                .build();
    }

    @Test
    void givenSubscribeRequestDTO_whenCreate_thenReturnSubscribeResponseDTO() throws Exception {

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(subscribeRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subscribeId").exists())
                .andExpect(jsonPath("$.type").value(subscribeRequestDTO.getType().toString()))
                .andExpect(jsonPath("$.name").value(subscribeRequestDTO.getName()))
                .andExpect(jsonPath("$.price").value(subscribeRequestDTO.getPrice()))
                .andExpect(jsonPath("$.storeId").value(subscribeRequestDTO.getStoreId()))
                .andExpect(jsonPath("$.intro").value(subscribeRequestDTO.getIntro()))
                .andExpect(jsonPath("$.isTop").value(subscribeRequestDTO.getIsTop()))
                .andExpect(jsonPath("$.useCount").value(subscribeRequestDTO.getUseCount()))
                .andExpect(jsonPath("$.createAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andExpect(jsonPath("$.benefits.[0].description").value(subscribeRequestDTO.getBenefits().get(0).getDescription()))
                .andExpect(jsonPath("$.benefits.[1].description").value(subscribeRequestDTO.getBenefits().get(1).getDescription()))
                .andExpect(jsonPath("$.benefits.[2].description").value(subscribeRequestDTO.getBenefits().get(2).getDescription()))
                .andDo(document("subscribe/create",
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("구독권 타입"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("구독권 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("구독권 가격"),
                                fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("구독권 가게 아이디"),
                                fieldWithPath("intro").type(JsonFieldType.STRING).description("구독권 간단소개"),
                                fieldWithPath("isTop").type(JsonFieldType.BOOLEAN).description("구독권 대표 여부"),
                                fieldWithPath("useCount").type(JsonFieldType.NUMBER).description("구독권 사용가능 횟수"),
                                fieldWithPath("benefits[].description").type(JsonFieldType.STRING).description("구독권 혜택")
                        ),
                        responseFields(
                                fieldWithPath("subscribeId").type(JsonFieldType.NUMBER).description("구독권 아이디"),
                                fieldWithPath("type").type(JsonFieldType.STRING).description("구독권 타입"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("구독권 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("구독권 가격"),
                                fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("구독권 가게 아이디"),
                                fieldWithPath("intro").type(JsonFieldType.STRING).description("구독권 간단소개"),
                                fieldWithPath("isTop").type(JsonFieldType.BOOLEAN).description("구독권 대표 여부"),
                                fieldWithPath("useCount").type(JsonFieldType.NUMBER).description("구독권 사용가능 횟수"),
                                fieldWithPath("createAt").type(JsonFieldType.STRING).description("구독권 생성날짜"),
                                fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("구독권 수정날짜"),
                                fieldWithPath("benefits[].description").type(JsonFieldType.STRING).description("구독권 혜택")
                        )
                ));
    }

    @Test
    void givenSubscribeId_whenGetSubscribe_thenReturnSubscribeResponseDTO() throws Exception{
        SubscribeResponseDTO subscribeResponseDTO = subscribeService.create(subscribeRequestDTO);

        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/{subscribeId}", subscribeResponseDTO.getSubscribeId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscribeId").exists())
                .andExpect(jsonPath("$.type").value(subscribeRequestDTO.getType().toString()))
                .andExpect(jsonPath("$.name").value(subscribeRequestDTO.getName()))
                .andExpect(jsonPath("$.price").value(subscribeRequestDTO.getPrice()))
                .andExpect(jsonPath("$.storeId").value(subscribeRequestDTO.getStoreId()))
                .andExpect(jsonPath("$.intro").value(subscribeRequestDTO.getIntro()))
                .andExpect(jsonPath("$.isTop").value(subscribeRequestDTO.getIsTop()))
                .andExpect(jsonPath("$.useCount").value(subscribeRequestDTO.getUseCount()))
                .andExpect(jsonPath("$.createAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andExpect(jsonPath("$.benefits.[0].description").value(subscribeRequestDTO.getBenefits().get(0).getDescription()))
                .andExpect(jsonPath("$.benefits.[1].description").value(subscribeRequestDTO.getBenefits().get(1).getDescription()))
                .andExpect(jsonPath("$.benefits.[2].description").value(subscribeRequestDTO.getBenefits().get(2).getDescription()))
                .andDo(document("subscribe/get",
                        pathParameters(
                                parameterWithName("subscribeId").description("구독권 아이디")
                        ),
                        responseFields(
                                fieldWithPath("subscribeId").type(JsonFieldType.NUMBER).description("구독권 아이디"),
                                fieldWithPath("type").type(JsonFieldType.STRING).description("구독권 타입"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("구독권 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("구독권 가격"),
                                fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("구독권 가게 아이디"),
                                fieldWithPath("intro").type(JsonFieldType.STRING).description("구독권 간단소개"),
                                fieldWithPath("isTop").type(JsonFieldType.BOOLEAN).description("구독권 대표 여부"),
                                fieldWithPath("useCount").type(JsonFieldType.NUMBER).description("구독권 사용가능 횟수"),
                                fieldWithPath("createAt").type(JsonFieldType.STRING).description("구독권 생성날짜"),
                                fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("구독권 수정날짜"),
                                fieldWithPath("benefits[].description").type(JsonFieldType.STRING).description("구독권 혜택")
                        )));
    }
}
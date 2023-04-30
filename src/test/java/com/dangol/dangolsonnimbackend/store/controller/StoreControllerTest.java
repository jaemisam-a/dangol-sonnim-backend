package com.dangol.dangolsonnimbackend.store.controller;

import com.amazonaws.util.IOUtils;
import com.dangol.dangolsonnimbackend.boss.dto.request.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.config.jwt.TokenProvider;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.domain.StoreImage;
import com.dangol.dangolsonnimbackend.store.dto.*;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.CategoryRepository;
import com.dangol.dangolsonnimbackend.store.repository.StoreImageRepository;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.store.service.MenuService;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import com.dangol.dangolsonnimbackend.subscribe.dto.BenefitDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.service.SubscribeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage.STORE_NOT_FOUND;
import static com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType.COUNT;
import static com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType.MONTHLY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(RestDocumentationExtension.class) // RestDocumentation 기능을 사용하기 위한 어노테이션
class StoreControllerTest {

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
    private MenuService menuService;
    @Autowired
    private SubscribeService subscribeService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private StoreImageRepository storeImageRepository;
    @Autowired
    private StoreRepository storeRepository;

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

    FieldDescriptor[] findDetailResponseJsonField = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("가게 아이디"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("가게 이름"),
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
            fieldWithPath("businessHours[].hours").type(JsonFieldType.STRING).description("영업 시간"),
            fieldWithPath("menuResponseDTOList[].storeId").type(JsonFieldType.NUMBER).description("메뉴가 속한 가게 ID"),
            fieldWithPath("menuResponseDTOList[].menuId").type(JsonFieldType.NUMBER).description("메뉴 ID"),
            fieldWithPath("menuResponseDTOList[].name").type(JsonFieldType.STRING).description("메뉴 이름"),
            fieldWithPath("menuResponseDTOList[].price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
            fieldWithPath("menuResponseDTOList[].imageUrl").type(JsonFieldType.STRING).description("메뉴 이미지 URL"),
            fieldWithPath("subscribeResponseDTOList[].type").type(JsonFieldType.STRING).description("구독 타입"),
            fieldWithPath("subscribeResponseDTOList[].subscribeId").type(JsonFieldType.NUMBER).description("구독 ID"),
            fieldWithPath("subscribeResponseDTOList[].storeId").type(JsonFieldType.NUMBER).description("구독이 속한 가게 ID"),
            fieldWithPath("subscribeResponseDTOList[].name").type(JsonFieldType.STRING).description("구독 이름"),
            fieldWithPath("subscribeResponseDTOList[].price").type(JsonFieldType.NUMBER).description("구독 가격"),
            fieldWithPath("subscribeResponseDTOList[].intro").type(JsonFieldType.STRING).description("구독 소개"),
            fieldWithPath("subscribeResponseDTOList[].isTop").type(JsonFieldType.BOOLEAN).description("구독 우선순위 여부"),
            fieldWithPath("subscribeResponseDTOList[].useCount").type(JsonFieldType.NUMBER).optional().description("이용 횟수"),
            fieldWithPath("subscribeResponseDTOList[].createAt").type(JsonFieldType.STRING).description("구독 생성 일시"),
            fieldWithPath("subscribeResponseDTOList[].modifiedAt").type(JsonFieldType.STRING).description("구독 수정 일시"),
            fieldWithPath("subscribeResponseDTOList[].benefits[].description").type(JsonFieldType.STRING).description("혜택 설명"),
            fieldWithPath("storeImageUrlList").type(JsonFieldType.ARRAY).description("가게 이미지 URL 리스트")
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

    private List<FieldDescriptor> pageableResponseFields() {
        return Arrays.asList(
                fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬이 비어 있는지 여부"),
                fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
                fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
                fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("페이지 시작 인덱스"),
                fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지되지 않았는지 여부"),
                fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지되었는지 여부"),
                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수"),
                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                fieldWithPath("number").type(JsonFieldType.NUMBER).description("페이지 번호"),
                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬이 비어 있는지 여부"),
                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("페이지의 요소 개수"),
                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("페이지가 비어 있는지 여부")
        );
    }

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
        Store store = storeRepository.findById(id).get();
        // 메뉴 생성
        MenuRequestDTO menuRequestDTO1 = MenuRequestDTO.builder()
                .name("김치찌개")
                .price(BigDecimal.valueOf(12000))
                .storeId(id)
                .multipartFile(null)
                .build();
        MenuRequestDTO menuRequestDTO2 = MenuRequestDTO.builder()
                .name("부대찌개")
                .price(BigDecimal.valueOf(10000))
                .storeId(id)
                .multipartFile(null)
                .build();
        menuService.create(menuRequestDTO1);
        menuService.create(menuRequestDTO2);

        // 구독권 생성
        SubscribeRequestDTO subscribeRequestDTO1 = SubscribeRequestDTO.builder()
                .isTop(true)
                .useCount(5)
                .type(COUNT)
                .storeId(id)
                .price(BigDecimal.valueOf(12000))
                .intro("INTRO TEST")
                .name("NAME TEST")
                .benefits(List.of(new BenefitDTO("혜택 1"), new BenefitDTO("혜택 2")))
                .build();

        SubscribeRequestDTO subscribeRequestDTO2 = SubscribeRequestDTO.builder()
                .isTop(true)
                .type(MONTHLY)
                .storeId(id)
                .price(BigDecimal.valueOf(10000))
                .intro("INTRO TEST")
                .name("NAME TEST")
                .benefits(List.of(new BenefitDTO("혜택 3"), new BenefitDTO("혜택 4")))
                .build();

        subscribeService.create(subscribeRequestDTO1);
        subscribeService.create(subscribeRequestDTO2);

        // 가게 이미지 저장
        StoreImage storeImage1 = storeImageRepository.save(new StoreImage(store,"이미지1 URL"));
        StoreImage storeImage2 = storeImageRepository.save(new StoreImage(store,"이미지2 URL"));
        store.getStoreImages().add(storeImage1);
        store.getStoreImages().add(storeImage2);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/store/find/{storeId}", id))
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
                .andExpect(jsonPath("$.menuResponseDTOList").exists())
                .andExpect(jsonPath("$.subscribeResponseDTOList").exists())
                .andExpect(jsonPath("$.storeImageUrlList").exists())
                .andDo(document("store/find",
                        pathParameters(
                                parameterWithName("storeId").description("가게 아이디")),
                        responseFields(findDetailResponseJsonField)
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

    @Test
    @Transactional
    @DisplayName("시군구와 카테고리 값을 통해 가게 목록을 검색할 수 있다.")
    void givenSigunguAndCategory_whenFindStoreList_thenReturnStoreList() throws Exception {
        // given
        storeService.create(dto, BOSS_TEST_EMAIL);

        String sigungu = "서초구";
        CategoryType categoryType = CategoryType.KOREAN;
        String kw = "골손";

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/store/list")
                        .param("sigungu", sigungu)
                        .param("category", categoryType.name())
                        .param("kw", kw)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].name").value(dto.getName()))
                .andExpect(jsonPath("$.content[0].newAddress").value(dto.getNewAddress()))
                .andExpect(jsonPath("$.content[0].comments").value(dto.getComments()))
                .andExpect(jsonPath("$.content[0].sigungu").value(dto.getSigungu()))
                .andExpect(jsonPath("$.content[0].bname1").value(dto.getBname1()))
                .andExpect(jsonPath("$.content[0].bname2").value(dto.getBname2()))
                .andExpect(jsonPath("$.content[0].detailedAddress").value(dto.getDetailedAddress()))
                .andExpect(jsonPath("$.content[0].categoryType").value(dto.getCategoryType().toString()))
                .andExpect(jsonPath("$.content[0].tags[0]").exists())
                .andExpect(jsonPath("$.content[0].businessHours[0].weeks").value(dto.getBusinessHours().get(0).getWeeks()))
                .andExpect(jsonPath("$.content[0].businessHours[0].hours").value(dto.getBusinessHours().get(0).getHours()))
                .andExpect(jsonPath("$.content[0].businessHours[1].weeks").value(dto.getBusinessHours().get(1).getWeeks()))
                .andExpect(jsonPath("$.content[0].businessHours[1].hours").value(dto.getBusinessHours().get(1).getHours()))
                .andDo(document("store/list",
                        requestParameters(
                                parameterWithName("sigungu").description("시군구"),
                                parameterWithName("category").description("카테고리"),
                                parameterWithName("kw").description("검색 키워드")),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("가게 아이디"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("content[].newAddress").type(JsonFieldType.STRING).description("가게 주소"),
                                fieldWithPath("content[].comments").type(JsonFieldType.STRING).description("가게 한줄평"),
                                fieldWithPath("content[].sido").type(JsonFieldType.STRING).description("가게 주소 (시/도)"),
                                fieldWithPath("content[].sigungu").type(JsonFieldType.STRING).description("가게 주소 (시/군/구)"),
                                fieldWithPath("content[].bname1").type(JsonFieldType.STRING).description("가게 주소 (읍/면)"),
                                fieldWithPath("content[].bname2").type(JsonFieldType.STRING).description("가게 주소 (동/리)"),
                                fieldWithPath("content[].detailedAddress").type(JsonFieldType.STRING).description("가게 상세주소"),
                                fieldWithPath("content[].categoryType").type(JsonFieldType.VARIES).description("카테고리 정보"),
                                fieldWithPath("content[].registerNumber").type(JsonFieldType.STRING).description("가게 사업자번호"),
                                fieldWithPath("content[].registerName").type(JsonFieldType.STRING).description("가게 사업자명"),
                                fieldWithPath("content[].tags").type(JsonFieldType.ARRAY).description("가게 태그"),
                                fieldWithPath("content[].businessHours[].weeks").type(JsonFieldType.STRING).description("영업 요일"),
                                fieldWithPath("content[].businessHours[].hours").type(JsonFieldType.STRING).description("영업 시간"),
                                fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 기준이 비어 있는지 여부"),
                                fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 기준이 정렬되어 있는지 여부"),
                                fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 기준이 정렬되지 않았는지 여부"),
                                fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("페이지 시작 위치"),
                                fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징되지 않았는지 여부"),
                                fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징되었는지 여부"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지인지 여부"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 항목 수"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 기준이 비어 있는지 여부"),
                                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 기준이 정렬되어 있는지 여부"),
                                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 기준이 정렬되지 않았는지 여부"),
                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 번째 페이지인지 여부"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 항목 수"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("페이지가 비어 있는지 여부")

                        )
                ));
    }

    @Test
    void imageUpload() throws Exception {
        Long id = storeService.create(dto, BOSS_TEST_EMAIL).getId();
        InputStream imageInputStream = getClass().getResourceAsStream("/example-image.png");
        byte[] imageBytes = IOUtils.toByteArray(imageInputStream);

        // MockMultipartFile 객체 생성
        MockMultipartFile multipartFile1 = new MockMultipartFile("multipartFile", "example-image.png", "image/png", imageBytes);
        MockMultipartFile multipartFile2 = new MockMultipartFile("multipartFile", "example-image.png", "image/png", imageBytes);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/store/image-upload")
                        .file(multipartFile1)
                        .file(multipartFile2)
                        .param("storeId", id.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("store/image-upload",
                        requestParts(
                                partWithName("multipartFile").description("가게 이미지 파일")
                        ),
                        requestParameters(
                                parameterWithName("storeId").description("가게 ID"),
                                parameterWithName("_csrf").description("CSRF 토큰 정보")
                        )
                ));
    }
}

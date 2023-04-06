package com.dangol.dangolsonnimbackend.store.controller;

import com.amazonaws.util.IOUtils;
import com.dangol.dangolsonnimbackend.boss.dto.request.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.store.domain.Menu;
import com.dangol.dangolsonnimbackend.store.dto.*;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.MenuRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.MenuQueryRepository;
import com.dangol.dangolsonnimbackend.store.service.MenuService;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(RestDocumentationExtension.class)
class MenuControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StoreService storeService;
    @Autowired
    private BossService bossService;
    @Autowired
    private MenuService menuService;
    @Autowired private MenuRepository menuRepository;
    @Autowired
    private MenuQueryRepository menuQueryRepository;
    private static final String BOSS_TEST_NAME = "GilDong";
    private static final String BOSS_TEST_EMAIL = "test@example.com";
    private static final String BOSS_TEST_PASSWORD = "password";
    private static final String BOSS_TEST_PHONE_NUMBER = "01012345678";
    private static final Boolean BOSS_TEST_MARKETING_AGREEMENT = true;

    private static final String BASE_URL = "/api/v1/menu";
    private Long storeId;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();

        BossSignupRequestDTO bossSignupRequestDTO = new BossSignupRequestDTO();
        bossSignupRequestDTO.setName(BOSS_TEST_NAME);
        bossSignupRequestDTO.setEmail(BOSS_TEST_EMAIL);
        bossSignupRequestDTO.setPassword(BOSS_TEST_PASSWORD);
        bossSignupRequestDTO.setPhoneNumber(BOSS_TEST_PHONE_NUMBER);
        bossSignupRequestDTO.setMarketingAgreement(BOSS_TEST_MARKETING_AGREEMENT);
        bossService.signup(bossSignupRequestDTO);


        // storeId를 생성하고 저장합니다. 이 storeId는 테스트에 사용됩니다.
        storeId = createStore();

        MenuService menuServiceMock = Mockito.mock(MenuService.class);

        // menuServiceMock의 create 메서드를 호출하면 가짜 메뉴를 반환하도록 합니다.
        when(menuServiceMock.create(any(MenuRequestDTO.class))).thenAnswer(invocation -> {
            MenuRequestDTO dto = invocation.getArgument(0);
            return MenuResponseDTO.builder()
                    .menuId(storeId)
                    .name(dto.getName())
                    .price(dto.getPrice())
                    .imageUrl("https://fake-s3-url/fake-image-url.jpg")
                    .build();
        });
    }

    private Long createStore() {
        StoreSignupRequestDTO dto = StoreSignupRequestDTO.builder()
                .name("단골손님" + new Random().nextInt())
                .phoneNumber("01012345678")
                .newAddress("서울특별시 서초구 단골로 130")
                .sido("서울특별시")
                .sigungu("서초구")
                .bname1("단골동")
                .bname2("")
                .detailedAddress("")
                .comments("단골손님 가게로 좋아요.")
                .registerNumber("1234567890")
                .registerName("단골손님")
                .categoryType(CategoryType.KOREAN)
                .tags(List.of("태그1", "태그2"))
                .businessHours(List.of(new BusinessHourRequestDTO("월~수", "10:00~12:00"),
                        new BusinessHourRequestDTO("토,일", "10:00~12:00")))
                .build();

        return storeService.create(dto, BOSS_TEST_EMAIL).getId();
    }

    @Test
    void create() throws Exception {
        InputStream imageInputStream = getClass().getResourceAsStream("/example-image.png");
        byte[] imageBytes = IOUtils.toByteArray(imageInputStream);

        // MockMultipartFile 객체 생성
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "example-image.png", "image/png", imageBytes);


        MenuRequestDTO dto = MenuRequestDTO.builder()
                .name("김치찌개")
                .price(BigDecimal.valueOf(12000))
                .storeId(storeId)
                .multipartFile(multipartFile)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/create")
                        .file(multipartFile)
                        .param("name", dto.getName())
                        .param("storeId", dto.getStoreId().toString())
                        .param("price", dto.getPrice().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.storeId").exists())
                .andExpect(jsonPath("$.menuId").exists())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.price").value(dto.getPrice()))
                .andExpect(jsonPath("$.imageUrl").exists())
                .andDo(document("menu/create",
                        requestParts(
                                partWithName("multipartFile").description("메뉴 이미지 파일")
                        ),
                        requestParameters(
                                parameterWithName("storeId").description("가게 ID"),
                                parameterWithName("name").description("메뉴 이름"),
                                parameterWithName("price").description("메뉴 가격"),
                                parameterWithName("_csrf").description("CSRF 토큰 정보")
                        ),
                        responseFields(
                                fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("menuId").type(JsonFieldType.NUMBER).description("메뉴 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("메뉴 이미지 URL")
                        )
                ));
    }

    @Test
    void findById() throws Exception {
        MenuRequestDTO dto = MenuRequestDTO.builder()
                .name("김치찌개")
                .price(BigDecimal.valueOf(12000))
                .storeId(storeId)
                .multipartFile(null)
                .build();
        MenuResponseDTO createdMenu = menuService.create(dto);
        Menu menu = menuQueryRepository.findById(createdMenu.getMenuId()).get();
        menu.setImageUrl("https://dangol.s3.ap-northeast-2.amazonaws.com/static/ccd32230");
        menuRepository.saveAndFlush(menu);

        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/find/{menuId}", createdMenu.getMenuId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(storeId))
                .andExpect(jsonPath("$.menuId").value(createdMenu.getMenuId()))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.price").value(dto.getPrice()))
                .andExpect(jsonPath("$.imageUrl").value("https://dangol.s3.ap-northeast-2.amazonaws.com/static/ccd32230"))
                .andDo(document("menu/find",
                        pathParameters(
                                parameterWithName("menuId").description("메뉴 ID")
                        ),
                        responseFields(
                                fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("menuId").type(JsonFieldType.NUMBER).description("메뉴 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("메뉴 이미지 URL")
                        )
                ));
    }

    @Test
    void update() throws Exception {
        InputStream imageInputStream = getClass().getResourceAsStream("/example-image.png");
        byte[] imageBytes = IOUtils.toByteArray(imageInputStream);

        // MockMultipartFile 객체 생성
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "example-image.png", "image/png", imageBytes);

        MenuRequestDTO createDto = MenuRequestDTO.builder()
                .name("김치찌개")
                .price(BigDecimal.valueOf(12000))
                .storeId(storeId)
                .multipartFile(null)
                .build();

        MenuResponseDTO createdMenu = menuService.create(createDto);

        MenuUpdateRequestDTO updateDto = MenuUpdateRequestDTO.builder()
                .menuId(createdMenu.getMenuId())
                .name("된장찌개")
                .price(BigDecimal.valueOf(10000))
                .multipartFile(multipartFile)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(BASE_URL + "/update")
                        .file(multipartFile)
                        .param("menuId", updateDto.getMenuId().toString())
                        .param("name", updateDto.getName())
                        .param("price", updateDto.getPrice().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.menuId").value(updateDto.getMenuId()))
                .andExpect(jsonPath("$.name").value(updateDto.getName()))
                .andExpect(jsonPath("$.price").value(updateDto.getPrice()))
                .andExpect(jsonPath("$.imageUrl").exists())
                .andDo(document("menu/update",
                        requestParts(
                                partWithName("multipartFile").description("메뉴 이미지 파일 (optional)")
                        ),
                        requestParameters(
                                parameterWithName("menuId").description("메뉴 ID"),
                                parameterWithName("name").description("메뉴 이름"),
                                parameterWithName("price").description("메뉴 가격"),
                                parameterWithName("_csrf").description("CSRF 토큰 정보")
                        ),
                        responseFields(
                                fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("menuId").type(JsonFieldType.NUMBER).description("메뉴 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("메뉴 이미지 URL")
                        )
                ));
    }

    @Test
    void delete() throws Exception {
        MenuRequestDTO createDto = MenuRequestDTO.builder()
                .name("김치찌개")
                .price(BigDecimal.valueOf(12000))
                .storeId(storeId)
                .multipartFile(null)
                .build();

        MenuResponseDTO createdMenu = menuService.create(createDto);

        mockMvc.perform(RestDocumentationRequestBuilders.delete(BASE_URL + "/delete/{menuId}", createdMenu.getMenuId())
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(document("menu/delete",
                        pathParameters(
                                parameterWithName("menuId").description("메뉴 ID")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("CSRF 토큰 정보")
                        )
                ));

        // 메뉴가 정상적으로 삭제되었는지 확인
        assertFalse(menuRepository.existsById(createdMenu.getMenuId()));
    }
}
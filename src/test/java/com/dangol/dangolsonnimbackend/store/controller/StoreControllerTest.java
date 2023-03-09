package com.dangol.dangolsonnimbackend.store.controller;

import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Random;

import static com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage.STORE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    private StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper;

    private StoreSignupRequestDTO dto;

    @Autowired
    private WebApplicationContext context;

    FieldDescriptor[] signUpRequestJsonField = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("가게 이름"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("가게 전화번호"),
            fieldWithPath("newAddress").type(JsonFieldType.STRING).description("가게 주소"),
            fieldWithPath("sido").type(JsonFieldType.STRING).description("가게 주소 (시/도)"),
            fieldWithPath("sigungu").type(JsonFieldType.STRING).description("가게 주소 (시/군/구)"),
            fieldWithPath("bname1").type(JsonFieldType.STRING).optional().description("가게 주소 (읍/면)"),
            fieldWithPath("bname2").type(JsonFieldType.STRING).optional().description("가게 주소 (동/리)"),
            fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("가게 카테고리"),
            fieldWithPath("detailedAddress").type(JsonFieldType.STRING).optional().description("가게 상세주소"),
            fieldWithPath("comments").type(JsonFieldType.STRING).description("가게 한줄평"),
            fieldWithPath("officeHours").type(JsonFieldType.STRING).description("가게 영업시간"),
            fieldWithPath("registerNumber").type(JsonFieldType.STRING).description("가게 사업자번호"),
            fieldWithPath("registerName").type(JsonFieldType.STRING).description("가게 사업자명")
    };

    FieldDescriptor[] findResponseJsonField = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("가게 아이디"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("가게 이름"),
//            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("가게 전화번호"),
            fieldWithPath("newAddress").type(JsonFieldType.STRING).description("가게 주소"),
            fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("가게 카테고리"),
            fieldWithPath("comments").type(JsonFieldType.STRING).description("가게 한줄평"),
            fieldWithPath("sido").type(JsonFieldType.STRING).description("가게 주소 (시/도)"),
            fieldWithPath("sigungu").type(JsonFieldType.STRING).description("가게 주소 (시/군/구)"),
            fieldWithPath("bname1").type(JsonFieldType.STRING).description("가게 주소 (읍/면)"),
            fieldWithPath("bname2").type(JsonFieldType.STRING).description("가게 주소 (동/리)"),
            fieldWithPath("detailedAddress").type(JsonFieldType.STRING).description("가게 상세주소"),
//            fieldWithPath("officeHours").type(JsonFieldType.STRING).description("가게 영업시간"),
            fieldWithPath("registerNumber").type(JsonFieldType.STRING).description("가게 사업자번호"),
            fieldWithPath("registerName").type(JsonFieldType.STRING).description("가게 사업자명")
    };

    FieldDescriptor[] updateRequestJsonField = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).optional().description("가게 이름"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).optional().description("가게 전화번호"),
            fieldWithPath("newAddress").type(JsonFieldType.STRING).optional().description("가게 주소"),
            fieldWithPath("sido").type(JsonFieldType.STRING).optional().description("가게 주소 (시/도)"),
            fieldWithPath("sigungu").type(JsonFieldType.STRING).optional().description("가게 주소 (시/군/구)"),
            fieldWithPath("bname1").type(JsonFieldType.STRING).optional().description("가게 주소 (읍/면)"),
            fieldWithPath("bname2").type(JsonFieldType.STRING).optional().description("가게 주소 (동/리)"),
            fieldWithPath("categoryId").type(JsonFieldType.NUMBER).optional().description("가게 카테고리"),
            fieldWithPath("detailedAddress").type(JsonFieldType.STRING).optional().description("가게 상세주소"),
            fieldWithPath("comments").type(JsonFieldType.STRING).optional().description("가게 한줄평"),
            fieldWithPath("officeHours").type(JsonFieldType.STRING).optional().description("가게 영업시간"),
            fieldWithPath("registerNumber").type(JsonFieldType.STRING).description("가게 사업자번호"),
            fieldWithPath("registerName").type(JsonFieldType.STRING).optional().description("가게 사업자명")
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
                .officeHours("08:00~10:00")
                .categoryId(1L)
                .registerNumber("1234567890")
                .registerName("단골손님")
                .build();
    }

    @Test
    @Transactional
    @DisplayName("새로운 가게 정보 생성을 요청하면 정상적으로 생성이 된다.")
    void givenSignupDto_whenSignup_thenCreateNewStore() throws Exception {
        mockMvc.perform(post("/api/v1/store/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.newAddress").value(dto.getNewAddress()))
                .andExpect(jsonPath("$.categoryId").value(dto.getCategoryId()))
                .andExpect(jsonPath("$.comments").value(dto.getComments()))
                .andExpect(jsonPath("$.sigungu").value(dto.getSigungu()))
                .andExpect(jsonPath("$.bname1").value(dto.getBname1()))
                .andExpect(jsonPath("$.bname2").value(dto.getBname2()))
                .andExpect(jsonPath("$.detailedAddress").value(dto.getDetailedAddress()))
                .andDo(document("store/create",
                        requestFields(signUpRequestJsonField)
                ));

        // 재등록 시에 이미 존재하는 사업자 번호로 반환
        assertThrows(BadRequestException.class, () -> storeService.signup(dto));
    }

    @Test
    @Transactional
    @DisplayName("특정 가게 아이디 값을 통해 가게를 검색할 수 있다.")
    void givenStoreId_whenFindById_thenReturnStore() throws Exception {
        Long id = storeService.signup(dto).getId();

        mockMvc.perform(get("/api/v1/store/find?id="+ id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.newAddress").value(dto.getNewAddress()))
                .andExpect(jsonPath("$.categoryId").value(dto.getCategoryId()))
                .andExpect(jsonPath("$.comments").value(dto.getComments()))
                .andExpect(jsonPath("$.sigungu").value(dto.getSigungu()))
                .andExpect(jsonPath("$.bname1").value(dto.getBname1()))
                .andExpect(jsonPath("$.bname2").value(dto.getBname2()))
                .andExpect(jsonPath("$.detailedAddress").value(dto.getDetailedAddress()))
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
        String registerNumber = storeService.signup(dto).getRegisterNumber();

        StoreUpdateDTO updateDTO = new StoreUpdateDTO(registerNumber);

        updateDTO.setName(Optional.of("단골손님" + new Random().nextInt()));
        updateDTO.setSido(Optional.of("경기도"));

        mockMvc.perform(patch("/api/v1/store/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDTO.getName().get()))
                .andExpect(jsonPath("$.sido").value(updateDTO.getSido().get()))
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
}

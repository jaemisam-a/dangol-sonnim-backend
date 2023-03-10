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
@ExtendWith(RestDocumentationExtension.class) // RestDocumentation ????????? ???????????? ?????? ???????????????
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
            fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("?????? ????????????"),
            fieldWithPath("newAddress").type(JsonFieldType.STRING).description("?????? ??????"),
            fieldWithPath("sido").type(JsonFieldType.STRING).description("?????? ?????? (???/???)"),
            fieldWithPath("sigungu").type(JsonFieldType.STRING).description("?????? ?????? (???/???/???)"),
            fieldWithPath("bname1").type(JsonFieldType.STRING).optional().description("?????? ?????? (???/???)"),
            fieldWithPath("bname2").type(JsonFieldType.STRING).optional().description("?????? ?????? (???/???)"),
            fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("?????? ????????????"),
            fieldWithPath("detailedAddress").type(JsonFieldType.STRING).optional().description("?????? ????????????"),
            fieldWithPath("comments").type(JsonFieldType.STRING).description("?????? ?????????"),
            fieldWithPath("officeHours").type(JsonFieldType.STRING).description("?????? ????????????"),
            fieldWithPath("registerNumber").type(JsonFieldType.STRING).description("?????? ???????????????"),
            fieldWithPath("registerName").type(JsonFieldType.STRING).description("?????? ????????????")
    };

    FieldDescriptor[] findResponseJsonField = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
//            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("?????? ????????????"),
            fieldWithPath("newAddress").type(JsonFieldType.STRING).description("?????? ??????"),
            fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("?????? ????????????"),
            fieldWithPath("comments").type(JsonFieldType.STRING).description("?????? ?????????"),
            fieldWithPath("sido").type(JsonFieldType.STRING).description("?????? ?????? (???/???)"),
            fieldWithPath("sigungu").type(JsonFieldType.STRING).description("?????? ?????? (???/???/???)"),
            fieldWithPath("bname1").type(JsonFieldType.STRING).description("?????? ?????? (???/???)"),
            fieldWithPath("bname2").type(JsonFieldType.STRING).description("?????? ?????? (???/???)"),
            fieldWithPath("detailedAddress").type(JsonFieldType.STRING).description("?????? ????????????"),
//            fieldWithPath("officeHours").type(JsonFieldType.STRING).description("?????? ????????????"),
            fieldWithPath("registerNumber").type(JsonFieldType.STRING).description("?????? ???????????????"),
            fieldWithPath("registerName").type(JsonFieldType.STRING).description("?????? ????????????")
    };

    FieldDescriptor[] updateRequestJsonField = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).optional().description("?????? ??????"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).optional().description("?????? ????????????"),
            fieldWithPath("newAddress").type(JsonFieldType.STRING).optional().description("?????? ??????"),
            fieldWithPath("sido").type(JsonFieldType.STRING).optional().description("?????? ?????? (???/???)"),
            fieldWithPath("sigungu").type(JsonFieldType.STRING).optional().description("?????? ?????? (???/???/???)"),
            fieldWithPath("bname1").type(JsonFieldType.STRING).optional().description("?????? ?????? (???/???)"),
            fieldWithPath("bname2").type(JsonFieldType.STRING).optional().description("?????? ?????? (???/???)"),
            fieldWithPath("categoryId").type(JsonFieldType.NUMBER).optional().description("?????? ????????????"),
            fieldWithPath("detailedAddress").type(JsonFieldType.STRING).optional().description("?????? ????????????"),
            fieldWithPath("comments").type(JsonFieldType.STRING).optional().description("?????? ?????????"),
            fieldWithPath("officeHours").type(JsonFieldType.STRING).optional().description("?????? ????????????"),
            fieldWithPath("registerNumber").type(JsonFieldType.STRING).description("?????? ???????????????"),
            fieldWithPath("registerName").type(JsonFieldType.STRING).optional().description("?????? ????????????")
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
                .name("????????????" + new Random().nextInt())
                .phoneNumber("01012345678")
                .newAddress("??????????????? ????????? ????????? 130")
                .sido("???????????????")
                .sigungu("?????????")
                .bname1("?????????")
                .bname2("")
                .detailedAddress("")
                .comments("???????????? ????????? ?????????.")
                .officeHours("08:00~10:00")
                .categoryId(1L)
                .registerNumber("1234567890")
                .registerName("????????????")
                .build();
    }

    @Test
    @Transactional
    @DisplayName("????????? ?????? ?????? ????????? ???????????? ??????????????? ????????? ??????.")
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

        // ????????? ?????? ?????? ???????????? ????????? ????????? ??????
        assertThrows(BadRequestException.class, () -> storeService.signup(dto));
    }

    @Test
    @Transactional
    @DisplayName("?????? ?????? ????????? ?????? ?????? ????????? ????????? ??? ??????.")
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
                                parameterWithName("id").description("?????? ?????????"),
                                parameterWithName("_csrf").description("CSRF ?????? ??????")),
                        responseFields(findResponseJsonField)

                ));
    }

    @Test
    @Transactional
    @DisplayName("?????? ?????? ????????? ?????????????????? ????????? ????????? ??? ??????.")
    void givenStore_whenUpdate_thenUpdateStore() throws Exception {
        String registerNumber = storeService.signup(dto).getRegisterNumber();

        StoreUpdateDTO updateDTO = new StoreUpdateDTO(registerNumber);

        updateDTO.setName(Optional.of("????????????" + new Random().nextInt()));
        updateDTO.setSido(Optional.of("?????????"));

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
    @DisplayName("???????????? ?????? ?????? ????????? ??????????????? ???????????? Bad Request??? ???????????????.")
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

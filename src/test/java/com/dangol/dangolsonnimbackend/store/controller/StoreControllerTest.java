package com.dangol.dangolsonnimbackend.store.controller;

import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Random;

import static com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage.STORE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper;

    private StoreSignupRequestDTO dto;

    @BeforeEach
    void setup() {
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
        mockMvc.perform(post("/api/v1/store/signup")
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
                .andExpect(jsonPath("$.detailedAddress").value(dto.getDetailedAddress()));

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
                .andExpect(jsonPath("$.detailedAddress").value(dto.getDetailedAddress()));
    }

    @Test
    @Transactional
    @DisplayName("특정 가게 정보를 업데이트하여 정보를 수정할 수 있다.")
    void givenStore_whenUpdate_thenUpdateStore() throws Exception {
        String registerNumber = storeService.signup(dto).getRegisterNumber();

        StoreUpdateDTO updateDTO = new StoreUpdateDTO(registerNumber);

        updateDTO.setName(Optional.of("단골손님" + new Random().nextInt()));
        updateDTO.setSido(Optional.of("경기도"));

        mockMvc.perform(put("/api/v1/store/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDTO.getName().get()))
                .andExpect(jsonPath("$.sido").value(updateDTO.getSido().get()));
    }

    @Test
    @Transactional
    @DisplayName("존재하지 않은 가게 정보로 업데이트를 요청하면 Bad Request를 반환받는다.")
    void givenNonExistedStore_whenUpdate_thenThrowException() throws Exception {
        StoreUpdateDTO updateDTO = new StoreUpdateDTO("None");

        mockMvc.perform(put("/api/v1/store/update")
                        .contentType(MediaType.APPLICATION_JSON)
                           .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(STORE_NOT_FOUND.getMessage()));
    }
}

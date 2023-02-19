package com.dangol.dangolsonnimbackend.store.domain;

import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StoreTest {
    @Test
    @DisplayName("가게 DTO 클래스로부터 엔티티 클래스가 정상적으로 생성이 되야 한다.")
    void testStoreSignupRequestDTOtoStoreConversion() {
        StoreSignupRequestDTO dto = StoreSignupRequestDTO.builder()
                .name("단골손님")
                .storePhoneNumber("01012345678")
                .newAddress("서울특별시 서초구 단골로 130")
                .sido("서울특별시")
                .sigungu("서초구")
                .bname1("단골동")
                .detailedAddress("")
                .comments("단골손님 가게로 좋아요.")
                .officeHours("08:00~10:00")
                .categoryId(1L)
                .storeRegisterNumber("123-456-789")
                .storeRegisterName("단골손님")
                .build();

        Store store = new Store(dto);

        assertEquals(dto.getName(), store.getName());
        assertEquals(dto.getStorePhoneNumber(), store.getPhoneNumber());
        assertEquals(dto.getNewAddress(), store.getNewAddress());
        assertEquals(dto.getSido(), store.getSido());
        assertEquals(dto.getSigungu(), store.getSigungu());
        assertEquals(dto.getBname1(), store.getBname1());
        assertNull(dto.getBname2());
        assertEquals(dto.getComments(), store.getComments());
        assertEquals(dto.getOfficeHours(), store.getOfficeHours());
        assertEquals(dto.getStoreRegisterNumber(), store.getRegisterNumber());
        assertEquals(dto.getStoreRegisterName(), store.getRegisterName());
    }
}

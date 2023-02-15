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
        StoreSignupRequestDTO dto = new StoreSignupRequestDTO();
        dto.setName("단골손님");
        dto.setStorePhoneNumber("01012345678");
        dto.setNewAddress("서울특별시 서초구 단골로 130");
        dto.setSido("서울특별시");
        dto.setSigungu("서초구");
        dto.setBname1("단골동");
        dto.setComments("단골손님 가게로 좋아요.");
        dto.setOfficeHours("08:00~10:00");

        Store store = new Store(dto);

        assertEquals(dto.getName(), store.getName());
        assertEquals(dto.getStorePhoneNumber(), store.getStorePhoneNumber());
        assertEquals(dto.getNewAddress(), store.getNewAddress());
        assertEquals(dto.getSido(), store.getSido());
        assertEquals(dto.getSigungu(), store.getSigungu());
        assertEquals(dto.getBname1(), store.getBname1());
        assertNull(dto.getBname2());
        assertEquals(dto.getComments(), store.getComments());
        assertEquals(dto.getOfficeHours(), store.getOfficeHours());
    }
}

package com.dangol.dangolsonnimbackend.store.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuUpdateRequestDTO {

    @NotNull(message = "메뉴 아이디는 Null 일 수 없습니다.")
    private Long menuId;

    private String name;

    private Long storeId;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private BigDecimal price;

    private MultipartFile multipartFile;
}

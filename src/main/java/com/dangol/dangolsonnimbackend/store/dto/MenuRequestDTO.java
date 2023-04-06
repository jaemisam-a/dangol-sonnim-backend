package com.dangol.dangolsonnimbackend.store.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDTO {

    @NotNull(message = "메뉴 이름은 Null 일 수 없습니다.")
    private String name;

    @NotNull
    private Long storeId;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000000")
    private BigDecimal price;

    private MultipartFile multipartFile;
}

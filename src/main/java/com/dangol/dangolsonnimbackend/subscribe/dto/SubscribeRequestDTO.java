package com.dangol.dangolsonnimbackend.subscribe.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SubscribeRequestDTO {

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;

    @NotNull(message = "가격은 Null 일 수 없습니다.")
    private BigDecimal price;

    @NotBlank(message = "간단소개는 공백일 수 없습니다.")
    private String intro;

    @NotNull(message = "대표 메뉴 여부는 Null 일 수 없습니다.")
    private Boolean isTop;

    private Integer useCount;

    private LocalDate period;
}

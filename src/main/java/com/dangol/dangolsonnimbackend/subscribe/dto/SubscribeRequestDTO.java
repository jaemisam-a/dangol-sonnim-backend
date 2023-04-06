package com.dangol.dangolsonnimbackend.subscribe.dto;

import com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeRequestDTO {

    @NotNull(message = "결제 타입은 Null 일 수 없습니다.")
    private SubscribeType type;

    @NotNull(message = "가게 아이디는 Null 일 수 없습니다.")
    private Long storeId;

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;

    @NotNull(message = "가격은 Null 일 수 없습니다.")
    private BigDecimal price;

    @NotBlank(message = "간단소개는 공백일 수 없습니다.")
    private String intro;

    @NotNull(message = "대표 메뉴 여부는 Null 일 수 없습니다.")
    private Boolean isTop;

    private Integer useCount;

    private List<BenefitDTO> benefits;
}

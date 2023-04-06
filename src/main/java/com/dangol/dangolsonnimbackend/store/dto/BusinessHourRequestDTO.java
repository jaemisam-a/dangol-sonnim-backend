package com.dangol.dangolsonnimbackend.store.dto;

import com.dangol.dangolsonnimbackend.store.domain.BusinessHour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessHourRequestDTO {
    @NotNull(message = "영업 요일은 Null 일 수 없습니다")
    private String weeks;
    @NotNull(message = "영업 시간은 Null 일 수 없습니다")
    private String hours;

    public BusinessHourRequestDTO(BusinessHour businessHour){
        this.weeks = businessHour.getWeeks();
        this.hours = businessHour.getHours();
    }
}

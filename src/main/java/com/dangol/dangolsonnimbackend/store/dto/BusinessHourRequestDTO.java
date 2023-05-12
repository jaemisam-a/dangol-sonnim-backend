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
    private String weeks;
    private String hours;

    public BusinessHourRequestDTO(BusinessHour businessHour){
        this.weeks = businessHour.getWeeks();
        this.hours = businessHour.getHours();
    }
}

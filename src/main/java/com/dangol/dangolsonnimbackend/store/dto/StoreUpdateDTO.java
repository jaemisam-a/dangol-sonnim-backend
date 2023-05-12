package com.dangol.dangolsonnimbackend.store.dto;

import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreUpdateDTO {
    private String name;
    private String phoneNumber;
    private String newAddress;
    private String sido;
    private String sigungu;
    private String bname1;
    private String bname2;
    private String detailedAddress;
    private String comments;
    private String registerNumber;
    private String registerName;
    private CategoryType categoryType;

    private List<String> tags;
    private List<BusinessHourRequestDTO> businessHours;

    public StoreUpdateDTO(String registerNumber) {
        this.registerNumber = registerNumber;
    }
}
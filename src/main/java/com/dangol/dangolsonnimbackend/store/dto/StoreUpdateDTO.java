package com.dangol.dangolsonnimbackend.store.dto;

import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreUpdateDTO {
    private Optional<String> name = Optional.empty();

    private Optional<String> phoneNumber = Optional.empty();

    private Optional<String> newAddress = Optional.empty();

    private Optional<String> sido = Optional.empty();

    private Optional<String> sigungu = Optional.empty();

    private Optional<String> bname1 = Optional.empty();

    private Optional<String> bname2 = Optional.empty();

    private Optional<String> detailedAddress = Optional.empty();

    private Optional<String> comments = Optional.empty();

    @NotNull
    private String registerNumber;

    private Optional<String> registerName = Optional.empty();

    private Optional<CategoryType> categoryType = Optional.empty();

    private List<String> tags;
    private List<BusinessHourRequestDTO> businessHours;

    public StoreUpdateDTO(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public StoreUpdateDTO name(String name) { this.name = Optional.of(name); return this; }
    public StoreUpdateDTO phoneNumber(String phoneNumber) { this.phoneNumber = Optional.of(phoneNumber); return this; }
    public StoreUpdateDTO newAddress(String newAddress) { this.newAddress = Optional.of(newAddress); return this; }
    public StoreUpdateDTO sido(String sido) { this.sido = Optional.of(sido); return this; }
    public StoreUpdateDTO sigungu(String sigungu) { this.sigungu = Optional.of(sigungu); return this; }
    public StoreUpdateDTO bname1(String bname1) { this.bname1 = Optional.of(bname1); return this; }
    public StoreUpdateDTO bname2(String bname2) { this.bname2 = Optional.of(bname2); return this; }
    public StoreUpdateDTO detailedAddress(String detailedAddress) { this.detailedAddress = Optional.of(detailedAddress); return this;}
    public StoreUpdateDTO comments(String comments) { this.comments = Optional.of(comments); return this; }
    public StoreUpdateDTO registerNumber(String registerNumber) { this.registerNumber = registerNumber; return this; }
    public StoreUpdateDTO registerName(String registerName) { this.registerName = Optional.of(registerName); return this; }
    public StoreUpdateDTO categoryType(CategoryType categoryType) { this.categoryType = Optional.of(categoryType); return this; }
}
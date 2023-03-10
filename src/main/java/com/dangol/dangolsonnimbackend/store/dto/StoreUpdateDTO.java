package com.dangol.dangolsonnimbackend.store.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
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

    private Optional<String> officeHours = Optional.empty();

    private Optional<Long> categoryId = Optional.empty();

    @NotNull
    private String registerNumber;

    private Optional<String> registerName = Optional.empty();

    private StoreUpdateDTO() { }

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
    public StoreUpdateDTO officeHours(String officeHours) { this.officeHours = Optional.of(officeHours); return this; }
    public StoreUpdateDTO registerNumber(String registerNumber) { this.registerNumber = registerNumber; return this; }
    public StoreUpdateDTO registerName(String registerName) { this.registerName = Optional.of(registerName); return this; }
}
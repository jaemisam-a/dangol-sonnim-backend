package com.dangol.dangolsonnimbackend.store.domain;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tb_store")
public class Store {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String newAddress;

    @Column(nullable = false)
    private String sido;

    @Column(nullable = false)
    private String sigungu;

    @Column
    private String bname1;

    @Column
    private String bname2;

    @Column
    private String detailedAddress;

    @Column(nullable = false)
    private String comments;

    @Column(nullable = false)
    private String officeHours;

    @OneToOne
    @JoinColumn(name="boss_id")
    private Boss boss;

    // TODO. 카테고리 엔티티 생성 시 JoinColumn 추가
    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private String registerNumber;

    @Column(nullable = false)
    private String registerName;

    public Store(StoreSignupRequestDTO dto) {
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();
        this.newAddress = dto.getNewAddress();
        this.sido = dto.getSido();
        this.sigungu = dto.getSigungu();
        this.bname1 = dto.getBname1();
        this.bname2 = dto.getBname2();
        this.detailedAddress = dto.getDetailedAddress();
        this.comments = dto.getComments();
        this.officeHours = dto.getOfficeHours();
        this.categoryId = dto.getCategoryId();
        this.registerName = dto.getRegisterName();
        this.registerNumber = dto.getRegisterNumber();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public Optional<Store> update(StoreUpdateDTO dto) {
        dto.getName().ifPresent(name -> this.name = name);
        dto.getPhoneNumber().ifPresent(phoneNumber -> this.phoneNumber = phoneNumber);
        dto.getNewAddress().ifPresent(newAddress -> this.newAddress = newAddress);
        dto.getSido().ifPresent(sido -> this.sido = sido);
        dto.getSigungu().ifPresent(sigungu -> this.sigungu = sigungu);
        dto.getBname1().ifPresent(bname1 -> this.bname1 = bname1);
        dto.getBname2().ifPresent(bname2 -> this.bname2 = bname2);
        dto.getDetailedAddress().ifPresent(detailedAddress -> this.detailedAddress = detailedAddress);
        dto.getComments().ifPresent(comments -> this.comments = comments);
        dto.getOfficeHours().ifPresent(officeHours -> this.officeHours = officeHours);
        dto.getCategoryId().ifPresent(categoryId -> this.categoryId = categoryId);
        dto.getRegisterName().ifPresent(registerName -> this.registerName = registerName);

        return Optional.of(this);
    }
}
package com.dangol.dangolsonnimbackend.store.domain;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boss_id", nullable = false)
    @JsonIgnore
    private Boss boss;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category")
    @ToString.Exclude
    private Category category;

    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private String registerNumber;

    @Column(nullable = false)
    private String registerName;

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menuList;

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BusinessHour> businessHours;
  
    @ManyToMany
    @JoinTable(
            name = "store_tag",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    
    @Column
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Subscribe> subscribeList = new ArrayList<>();

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
        this.registerName = dto.getRegisterName();
        this.registerNumber = dto.getRegisterNumber();
    }

    public Store(StoreSignupRequestDTO dto, Category category, Boss boss) {
        this(dto);
        this.category = category;
        this.category.addStore(this);
        this.boss = boss;
        this.boss.getStoreList().add(this);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateCategory(Category category) {
        this.category.removeStore(this);
        this.category = category;
        this.category.addStore(this);
    }

    public Optional<Store> update(StoreUpdateDTO dto, Optional<Category> category) {
        dto.getName().ifPresent(name -> this.name = name);
        dto.getPhoneNumber().ifPresent(phoneNumber -> this.phoneNumber = phoneNumber);
        dto.getNewAddress().ifPresent(newAddress -> this.newAddress = newAddress);
        dto.getSido().ifPresent(sido -> this.sido = sido);
        dto.getSigungu().ifPresent(sigungu -> this.sigungu = sigungu);
        dto.getBname1().ifPresent(bname1 -> this.bname1 = bname1);
        dto.getBname2().ifPresent(bname2 -> this.bname2 = bname2);
        dto.getDetailedAddress().ifPresent(detailedAddress -> this.detailedAddress = detailedAddress);
        dto.getComments().ifPresent(comments -> this.comments = comments);
        dto.getRegisterName().ifPresent(registerName -> this.registerName = registerName);
        category.ifPresent(newCategory -> updateCategory(newCategory));

        return Optional.of(this);
    }

    public void setTags(Set<Tag> tags){
        this.tags = tags;
    }

    public void setBusinessHours(List<BusinessHour> businessHours) {
        this.businessHours = businessHours;
    }
}
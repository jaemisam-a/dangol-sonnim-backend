package com.dangol.dangolsonnimbackend.store.domain;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.customer.domain.Like;
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
@Table(name="tb_store",
        indexes = {
                @Index(name = "idx_sigungu", columnList = "sigungu"),
                @Index(name = "idx_category", columnList = "category"),
                @Index(name = "idx_name", columnList = "name")
        })
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

    @Column
    private Integer likeNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boss_id", nullable = false)
    @JsonIgnore
    private Boss boss;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category")
    private Category category;

    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private String registerNumber;

    @Column(nullable = false)
    private String registerName;

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Menu> menuList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BusinessHour> businessHours = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likeList = new ArrayList<>();
  
    @ManyToMany
    @JoinTable(
            name = "store_tag",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    
    @Column
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Subscribe> subscribeList = new ArrayList<>();

    @Column
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<StoreImage> storeImages = new ArrayList<>();

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
        this.likeNumber = 0;
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

    public Store update(StoreUpdateDTO dto, Category category) {
        this.name = dto.getName() != null ? dto.getName() : this.name;
        this.phoneNumber = dto.getPhoneNumber() != null ? dto.getPhoneNumber() : this.phoneNumber;
        this.newAddress = dto.getNewAddress() != null ? dto.getNewAddress() : this.newAddress;
        this.sido = dto.getSido() != null ? dto.getSido() : this.sido;
        this.sigungu = dto.getSigungu() != null ? dto.getSigungu() : this.sigungu;
        this.bname1 = dto.getBname1() != null ? dto.getBname1() : this.bname1;
        this.bname2 = dto.getBname2() != null ? dto.getBname2() : this.bname2;
        this.detailedAddress = dto.getDetailedAddress() != null ? dto.getDetailedAddress() : this.detailedAddress;
        this.comments = dto.getComments() != null ? dto.getComments() : this.comments;
        this.registerName = dto.getRegisterName() != null ? dto.getRegisterName() : this.registerName;
        if (category != null) {
            updateCategory(category);
        }
        return this;
    }

    public void setTags(Set<Tag> tags){
        this.tags = tags;
    }

    public void setBusinessHours(List<BusinessHour> businessHours) {
        this.businessHours.clear();
        this.businessHours.addAll(businessHours);
    }
    public void setStoreImages(List<StoreImage> storeImages) {
        this.storeImages.clear();
        if (storeImages != null) {
            this.storeImages.addAll(storeImages);
        }
    }

    public void increaseLikeNum() {
        this.likeNumber += 1;
    }

    public void decreaseLikeNum() {
        this.likeNumber -= 1;
    }
}
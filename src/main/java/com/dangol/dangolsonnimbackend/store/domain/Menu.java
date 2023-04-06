package com.dangol.dangolsonnimbackend.store.domain;

import com.dangol.dangolsonnimbackend.store.dto.MenuUpdateRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tb_menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Setter
    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonIgnore
    private Store store;

    @Builder
    public Menu(String name, String imageUrl, Store store, BigDecimal price){
        this.name = name;
        this.imageUrl = imageUrl;
        this.store = store;
        this.price = price;
    }

    public void update(MenuUpdateRequestDTO dto, String imageUrl){
        if (dto.getName() != null) this.name = dto.getName();
        if (dto.getPrice() != null) this.price = dto.getPrice();
        if (imageUrl != null) this.imageUrl = imageUrl;
    }
}

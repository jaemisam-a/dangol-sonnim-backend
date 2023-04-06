package com.dangol.dangolsonnimbackend.store.dto;

import com.dangol.dangolsonnimbackend.store.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MenuResponseDTO {

    private Long menuId;

    private String imageUrl;

    private Long storeId;

    private BigDecimal price;
    private String name;

    public MenuResponseDTO(Menu menu){
        this.menuId = menu.getId();
        this.storeId = menu.getStore().getId();
        this.price = menu.getPrice();
        this.imageUrl = menu.getImageUrl();
        this.name = menu.getName();
    }
}

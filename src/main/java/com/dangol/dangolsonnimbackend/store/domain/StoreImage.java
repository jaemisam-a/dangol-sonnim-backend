package com.dangol.dangolsonnimbackend.store.domain;

import com.dangol.dangolsonnimbackend.store.dto.StoreImageUploadRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tb_store_image")
public class StoreImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonIgnore
    private Store store;

    public StoreImage(Store store, String imageUrl){
        this.imageUrl = imageUrl;
        this.store = store;
    }
}

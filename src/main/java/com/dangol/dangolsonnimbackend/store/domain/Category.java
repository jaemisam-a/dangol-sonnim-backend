package com.dangol.dangolsonnimbackend.store.domain;

import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Column
    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Store> store = new ArrayList<>();

    public void addStore(Store store) {
        this.store.add(store);
    }

    public void removeStore(Store store) {
        this.store.remove(store);
    }
}
package com.dangol.dangolsonnimbackend.subscribe.domain;

import com.dangol.dangolsonnimbackend.customer.domain.PurchasedSubscribe;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_subscribe")
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorColumn(name = "subscribe_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    private Boolean isTop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "subscribe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchasedSubscribe> subscribeList = new ArrayList<>();

    @Column
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Benefit> benefits = new ArrayList<>();

    protected Subscribe(String name, BigDecimal price, String intro,
                     Boolean isTop, Store store){
        this.name = name;
        this.price = price;
        this.intro = intro;
        this.isTop = isTop;
        this.store = store;
    }

    public abstract SubscribeResponseDTO toResponseDTO();
}
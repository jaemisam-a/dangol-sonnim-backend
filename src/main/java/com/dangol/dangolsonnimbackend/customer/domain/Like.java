package com.dangol.dangolsonnimbackend.customer.domain;

import com.dangol.dangolsonnimbackend.store.domain.Store;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_like")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Customer customer;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Like(Customer customer, Store store) {
        this.customer = customer;
        this.store = store;
    }
}
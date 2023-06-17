package com.dangol.dangolsonnimbackend.customer.domain;

import com.dangol.dangolsonnimbackend.customer.dto.PurchaseSubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_purchased_subscribe")
public class PurchasedSubscribe {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String merchantUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribe_id")
    private Subscribe subscribe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Customer customer;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    private Integer count;

    @Column
    private LocalDateTime expiredAt;

    public PurchasedSubscribe(CountSubscribe countSubscribe, Customer customer, PurchaseSubscribeRequestDTO dto){
        this.count = countSubscribe.getUseCount();
        this.merchantUid = dto.getMerchantUid();
        this.customer = customer;
        this.subscribe = countSubscribe;
    }

    public PurchasedSubscribe(MonthlySubscribe monthlySubscribe, Customer customer, PurchaseSubscribeRequestDTO dto){
        this.count = 99999999;
        this.merchantUid = dto.getMerchantUid();
        this.customer = customer;
        this.subscribe = monthlySubscribe;
        this.expiredAt = LocalDateTime.now().plusDays(30);
    }
}

package com.dangol.dangolsonnimbackend.customer.domain;

import com.dangol.dangolsonnimbackend.customer.dto.PurchaseSubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import com.dangol.dangolsonnimbackend.subscribe.dto.BenefitDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.PurchasedSubscribeResponseDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
import com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

    @Column
    private SubscribeType subscribeType;

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
    private Integer totalCount;

    @Column
    private Integer remainingCount;

    @Column
    private LocalDateTime expiredAt;

    @Column
    private String qrCodeImageUrl;

    public PurchasedSubscribe(CountSubscribe countSubscribe, Customer customer, PurchaseSubscribeRequestDTO dto){
        this.merchantUid = dto.getMerchantUid();
        this.customer = customer;
        this.subscribe = countSubscribe;
        this.totalCount = countSubscribe.getUseCount();
        this.remainingCount = countSubscribe.getUseCount();
        this.expiredAt = LocalDateTime.now().plusDays(30);
        this.subscribeType = SubscribeType.COUNT;
    }

    public PurchasedSubscribe(MonthlySubscribe monthlySubscribe, Customer customer, PurchaseSubscribeRequestDTO dto){
        this.merchantUid = dto.getMerchantUid();
        this.customer = customer;
        this.subscribe = monthlySubscribe;
        this.expiredAt = LocalDateTime.now().plusDays(30);
        this.subscribeType = SubscribeType.MONTHLY;
    }

    public PurchasedSubscribeResponseDTO toResponseDTO() {
        PurchasedSubscribeResponseDTO dto = new PurchasedSubscribeResponseDTO();
        dto.setPurchasedSubscribeId(this.id);
        dto.setMerchantUid(this.merchantUid);
        dto.setSubscribeType(this.subscribeType);
        dto.setStoreTitle(this.subscribe.getStore().getName());
        dto.setSigungu(this.subscribe.getStore().getSigungu());
        dto.setBname1(this.subscribe.getStore().getBname1());
        dto.setBname2(this.subscribe.getStore().getBname2());
        dto.setSubscribeName(this.subscribe.getName());
        dto.setBenefits(this.subscribe.getBenefits().stream()
                .map(BenefitDTO::new).collect(Collectors.toList()));
        dto.setQRImageUrl(this.qrCodeImageUrl);
        dto.setCreatedAt(String.valueOf(this.createdAt));
        dto.setExpiredAt(String.valueOf(this.expiredAt));

        if (this.subscribeType == SubscribeType.COUNT) {
            dto.setTotalCount(this.totalCount);
            dto.setRemainingCount(this.remainingCount);
        }
        dto.setIntro(this.subscribe.getIntro());
        dto.setPrice(this.subscribe.getPrice());

        return dto;
    }

    public void setQRImageUrl(String qrCodeImageUrl) {
        this.qrCodeImageUrl = qrCodeImageUrl;
    }
    public void useSubscribe(){
        this.remainingCount -= 1;
    }
}

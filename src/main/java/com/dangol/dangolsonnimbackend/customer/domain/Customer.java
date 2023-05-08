package com.dangol.dangolsonnimbackend.customer.domain;

import com.dangol.dangolsonnimbackend.oauth.ProviderType;
import com.dangol.dangolsonnimbackend.oauth.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_customer")
public class Customer {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(length = 64, unique = true)
    @NotNull
    @Size(max = 64)
    private String id; // 사용자에게 부여되는 고유 아이디

    @Setter
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private CustomerInfo customerInfo;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likeList = new ArrayList<>();;

    public Customer(String id, String name, String email, ProviderType providerType, RoleType roleType, CustomerInfo customerInfo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.providerType = providerType;
        this.roleType = roleType;
        this.customerInfo = customerInfo;
    }
}

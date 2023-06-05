package com.dangol.dangolsonnimbackend.customer.domain;

import com.dangol.dangolsonnimbackend.customer.dto.CustomerInfoRequestDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerUpdateRequestDTO;
import com.dangol.dangolsonnimbackend.oauth.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_customer_info")
public class CustomerInfo {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    private String profileImageUrl;

    @Column(unique = true)
    private String phoneNumber;

    @Column
    private String birth;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @Setter
    private Customer customer;

    public void setCustomerInfo(CustomerInfoRequestDTO dto, String profileImageUrl) {
        this.nickname = dto.getNickname();
        this.phoneNumber = dto.getPhoneNumber();
        this.birth = dto.getBirth();
        this.profileImageUrl = profileImageUrl;
    }

    public void update(CustomerUpdateRequestDTO dto, String s3FileUrl) {
        if (dto.getBirth() != null) this.birth = dto.getBirth();
        if (dto.getNickname() != null) this.nickname = dto.getNickname();
        if (dto.getPhoneNumber() != null) this.phoneNumber = dto.getPhoneNumber();
        if (s3FileUrl != null) this.profileImageUrl = s3FileUrl;
    }
}

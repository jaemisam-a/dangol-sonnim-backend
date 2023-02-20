package com.dangol.dangolsonnimbackend.customer.domain;

import com.dangol.dangolsonnimbackend.customer.dto.CustomerSignupRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(nullable = false, unique = true)
    private String nickname;

    private String profileImageUrl;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String birth;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public CustomerInfo(CustomerSignupRequestDTO dto) {
        this.nickname = dto.getNickname();
        this.profileImageUrl = dto.getProfileImageUrl();
        this.phoneNumber = dto.getPhoneNumber();
        this.birth = dto.getBirth();
    }
}

package com.dangol.dangolsonnimbackend.customer.dto;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.oauth.ProviderType;
import com.dangol.dangolsonnimbackend.oauth.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private String id;
    private String name;
    private String email;
    private ProviderType providerType;
    private RoleType roleType;
    private String nickname;
    private String profileImageUrl;
    private String phoneNumber;
    private String birth;

    private String createdAt;
    private String modifiedAt;

    public CustomerResponseDTO(Customer customer){
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.roleType = customer.getRoleType();
        this.providerType = customer.getProviderType();
        this.nickname = customer.getCustomerInfo().getNickname();
        this.profileImageUrl = customer.getCustomerInfo().getProfileImageUrl();
        this.phoneNumber = customer.getCustomerInfo().getPhoneNumber();
        this.birth = customer.getCustomerInfo().getBirth();
        this.createdAt = String.valueOf(customer.getCustomerInfo().getCreatedAt());
        this.modifiedAt = String.valueOf(customer.getCustomerInfo().getModifiedAt());
    }
}

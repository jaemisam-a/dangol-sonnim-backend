package com.dangol.dangolsonnimbackend.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequestDTO {

    @Size(min = 2, max = 12, message = "닉네임은 2 ~ 12자 사이여야 합니다.")
    private String nickname;

    @Size(min = 11, max = 11, message = "휴대폰 번호는 11자 이여야 합니다.")
    private String phoneNumber;

    @Size(min = 8, max = 8, message = "생년월일은 8자 이여야 합니다.")
    private String birth;

    private MultipartFile multipartFile;
}

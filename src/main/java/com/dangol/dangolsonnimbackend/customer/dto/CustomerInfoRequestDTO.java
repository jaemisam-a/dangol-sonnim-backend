package com.dangol.dangolsonnimbackend.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoRequestDTO {

    @NotNull(message = "닉네임은 Null 일 수 없습니다.")
    @Size(min = 2, max = 12, message = "닉네임은 2 ~ 12자 사이여야 합니다.")
    private String nickname;

    @NotNull(message = "휴대폰 번호는 Null 일 수 없습니다.")
    @Size(min = 11, max = 11, message = "휴대폰 번호는 11자 이여야 합니다.")
    private String phoneNumber;

    @NotNull(message = "생년월일은 Null 일 수 없습니다.")
    @Size(min = 8, max = 8, message = "생년월일은 8자 이여야 합니다.")
    private String birth;

    private MultipartFile multipartFile;
}

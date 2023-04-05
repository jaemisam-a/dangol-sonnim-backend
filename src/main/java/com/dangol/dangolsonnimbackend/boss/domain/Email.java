package com.dangol.dangolsonnimbackend.boss.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "tb_email")
public class Email {

    @Id
    private String bossEmail;
    private String authCode;

    public Email(String email, String authCode){
        this.bossEmail = email;
        this.authCode = authCode;
    }

    public void setAuthCode(String authCode){
        this.authCode = authCode;
    }
}

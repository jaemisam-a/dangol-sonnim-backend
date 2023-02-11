package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BossServiceImpl {

    private final BossRepository bossRepository;
    private final BossQueryRepository bossQueryRepository;
    private final PasswordEncoder passwordEncoder;

    public BossServiceImpl(BossRepository bossRepository, BossQueryRepository bossQueryRepository,
                           PasswordEncoder passwordEncoder){
        this.bossRepository = bossRepository;
        this.bossQueryRepository = bossQueryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(BossSignupRequestDTO dto){
        validateSignup(dto);

        dto.passwordEncode(passwordEncoder.encode(dto.getPassword()));
        bossRepository.save(new Boss(dto));
    }

    private void validateSignup(BossSignupRequestDTO dto) {
        if (bossQueryRepository.existsBySrn(dto.getStoreRegisterName())) {
            throw new RuntimeException();
        }
        if (bossQueryRepository.existsByBpn(dto.getBossPhoneNumber())) {
            throw new RuntimeException();
        }
        if (bossQueryRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException();
        }
    }
}

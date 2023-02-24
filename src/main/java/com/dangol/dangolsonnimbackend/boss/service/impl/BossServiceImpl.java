package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class BossServiceImpl implements BossService {

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

    @Transactional(readOnly = true)
    public Boss findByEmail(String email) {
        return Optional.ofNullable(bossQueryRepository.findByEmail(email)).orElseThrow(
                () -> new BadRequestException(ErrorCodeMessage.BOSS_NOT_FOUND)
        );
    }

    private void validateSignup(BossSignupRequestDTO dto) {
        if (bossQueryRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_STORE_REGISTER_NUMBER);
        }
        if (bossQueryRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_STORE_REGISTER_NUMBER);
        }
    }
}

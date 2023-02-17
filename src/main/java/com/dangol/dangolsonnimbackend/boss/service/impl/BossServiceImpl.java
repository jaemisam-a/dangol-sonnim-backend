package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninReqeustDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.config.jwt.TokenProvider;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class BossServiceImpl implements BossService {

    private final BossRepository bossRepository;
    private final BossQueryRepository bossQueryRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public BossServiceImpl(BossRepository bossRepository, BossQueryRepository bossQueryRepository,
                           PasswordEncoder passwordEncoder, TokenProvider tokenProvider){
        this.bossRepository = bossRepository;
        this.bossQueryRepository = bossQueryRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public void signup(BossSignupRequestDTO dto){
        validateSignup(dto);

        dto.passwordEncode(passwordEncoder.encode(dto.getPassword()));
        bossRepository.save(new Boss(dto));
    }

    @Transactional
    public BossSigninResponseDTO getByCredentials(BossSigninReqeustDTO reqeustDTO) {
        Boss boss = Optional.ofNullable(bossQueryRepository.findByEmail(reqeustDTO.getEmail())).orElseThrow(
                () -> new BadRequestException(ErrorCodeMessage.BOSS_NOT_FOUND));
        if(!passwordEncoder.matches(reqeustDTO.getPassword(), boss.getPassword())){
            throw new BadRequestException(ErrorCodeMessage.PASSWORD_NOT_MATCH);
        }

        final String token = tokenProvider.generateAccessToken(reqeustDTO.getEmail());
        return new BossSigninResponseDTO(token);
    }

    private void validateSignup(BossSignupRequestDTO dto) {
        if (bossQueryRepository.existsBySrn(dto.getStoreRegisterNumber())) {
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_STORE_REGISTER_NUMBER);
        }
    }
}

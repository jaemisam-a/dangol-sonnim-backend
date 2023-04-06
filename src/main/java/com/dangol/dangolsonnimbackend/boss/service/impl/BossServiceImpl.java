package com.dangol.dangolsonnimbackend.boss.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossFindEmailResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.*;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.config.jwt.TokenProvider;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
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
    public BossResponseDTO signup(BossSignupRequestDTO dto){
        validateSignup(dto);
        dto.passwordEncode(passwordEncoder.encode(dto.getPassword()));
        Boss boss = bossRepository.save(new Boss(dto));
        return new BossResponseDTO(boss);
    }

    @Transactional(readOnly = true)
    public Boss findByEmail(String email) {
        return Optional.ofNullable(bossQueryRepository.findByEmail(email)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.BOSS_NOT_FOUND)
        );
    }
    @Transactional
    public void withdraw(String email) {
        Boss boss = Optional.ofNullable(bossQueryRepository.findByEmail(email)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.BOSS_NOT_FOUND)
        );
        bossRepository.delete(boss);
    }

    @Transactional
    public BossSigninResponseDTO getByCredentials(BossSigninReqeustDTO reqeustDTO) {
        Boss boss = Optional.ofNullable(bossQueryRepository.findByEmail(reqeustDTO.getEmail())).orElseThrow(
                () -> new BadRequestException(ErrorCodeMessage.BOSS_NOT_FOUND));
        if (!passwordEncoder.matches(reqeustDTO.getPassword(), boss.getPassword())) {
            throw new BadRequestException(ErrorCodeMessage.PASSWORD_NOT_MATCH);
        }

        final String token = tokenProvider.generateAccessToken(reqeustDTO.getEmail());
        return new BossSigninResponseDTO(token);
    }

    @Transactional
    public BossResponseDTO update(String email, BossUpdateRequestDTO requestDTO){
        Boss boss = findByEmail(email);
        boss.updateInfo(requestDTO);
        return new BossResponseDTO(boss);
    }

    @Transactional
    public BossResponseDTO updatePassword(BossPasswordUpdateReqeuestDTO reqeuestDTO) {
        Boss boss = findByEmail(reqeuestDTO.getEmail());
        boss.updatePassword(passwordEncoder.encode(reqeuestDTO.getPassword()));
        return new BossResponseDTO(boss);
    }

    @Transactional(readOnly = true)
    public BossFindEmailResponseDTO findEmailByPhoneNumber(BossFindEmailReqeustDTO requestDTO){
        Boss boss = Optional.ofNullable(bossQueryRepository.findByPhoneNumber(requestDTO.getPhoneNumber())).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.BOSS_NOT_FOUND)
        );
        return new BossFindEmailResponseDTO(boss.getEmail());
    }

    private void validateSignup(BossSignupRequestDTO dto) {
        if (bossQueryRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException(ErrorCodeMessage.BOSS_NOT_FOUND);
        }
        if (bossQueryRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_STORE_REGISTER_NUMBER);
        }
    }
}

package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.dto.*;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/boss")
public class BossController {

    private final BossService bossService;

    public BossController(BossService bossService) {
        this.bossService = bossService;
    }

    @PostMapping
    public ResponseEntity<Void> signup(@Valid @RequestBody BossSignupRequestDTO dto) {
        bossService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<BossSigninResponseDTO> authenticate(@Valid @RequestBody BossSigninReqeustDTO reqeustDTO) {
        BossSigninResponseDTO responseDTO = bossService.getByCredentials(reqeustDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal String email) {
        bossService.withdraw(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<BossResponseDTO> getBoss(@AuthenticationPrincipal String email) {
        BossResponseDTO responseDTO = new BossResponseDTO(bossService.findByEmail(email));
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PatchMapping
    public ResponseEntity<BossResponseDTO> update(@AuthenticationPrincipal String email, @Valid @RequestBody BossUpdateRequestDTO reqeustDTO) {
        BossResponseDTO responseDTO = new BossResponseDTO(bossService.update(email, reqeustDTO));
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody BossPasswordUpdateReqeuestDTO reqeuestDTO){
        bossService.updatePassword(reqeuestDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/find-email")
    public ResponseEntity<BossFindEmailResponseDTO> findEmail(@Valid @RequestBody BossFindEmailReqeustDTO reqeustDTO){
        BossFindEmailResponseDTO responseDTO = bossService.findEmailByPhoneNumber(reqeustDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}

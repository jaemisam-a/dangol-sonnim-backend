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
    public ResponseEntity<?> signup(@Valid @RequestBody BossSignupRequestDTO dto) {
        bossService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody BossSigninReqeustDTO reqeustDTO) {
        BossSigninResponseDTO responseDTO = bossService.getByCredentials(reqeustDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal String email) {
        bossService.withdraw(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<?> getBoss(@AuthenticationPrincipal String email) {
        BossResponseDTO responseDTO = new BossResponseDTO(bossService.findByEmail(email));
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PatchMapping
    public ResponseEntity<?> update(@AuthenticationPrincipal String email, @Valid @RequestBody BossUpdateRequestDTO reqeustDTO) {
        BossResponseDTO responseDTO = new BossResponseDTO(bossService.update(email, reqeustDTO));
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody BossPasswordUpdateReqeuestDTO reqeuestDTO){
        bossService.updatePassword(reqeuestDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

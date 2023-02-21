package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.dto.BossResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninReqeustDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/boss")
public class BossController {

    private final BossService bossService;

    public BossController(BossService bossService) {
        this.bossService = bossService;
    }

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody BossSignupRequestDTO dto) {
        bossService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody BossSigninReqeustDTO reqeustDTO) {
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
}

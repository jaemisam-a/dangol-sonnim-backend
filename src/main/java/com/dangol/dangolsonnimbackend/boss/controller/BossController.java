package com.dangol.dangolsonnimbackend.boss.controller;

import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossFindEmailResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.reponse.BossSigninResponseDTO;
import com.dangol.dangolsonnimbackend.boss.dto.request.*;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/v1/boss")
public class BossController {

    private final BossService bossService;

    public BossController(BossService bossService) {
        this.bossService = bossService;
    }

    @PostMapping
    public ResponseEntity<BossResponseDTO> signup(@Valid @RequestBody BossSignupRequestDTO dto) {
        BossResponseDTO responseDTO = bossService.signup(dto)
                .add(
                        linkTo(methodOn(BossController.class).signup(dto)).withSelfRel(),
                        linkTo(methodOn(BossController.class).authenticate(null)).withRel("authenticate")
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/signin")
    public ResponseEntity<BossSigninResponseDTO> authenticate(@Valid @RequestBody BossSigninReqeustDTO reqeustDTO) {
        BossSigninResponseDTO responseDTO = bossService.getByCredentials(reqeustDTO)
                .add(
                        linkTo(methodOn(BossController.class).authenticate(reqeustDTO)).withSelfRel(),
                        linkTo(methodOn(BossController.class).getBoss(reqeustDTO.getEmail())).withRel("getBoss")
                );
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> withdrawBoss(@AuthenticationPrincipal String email) {
        bossService.withdraw(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<BossResponseDTO> getBoss(@AuthenticationPrincipal String email) {
        BossResponseDTO responseDTO = new BossResponseDTO(bossService.findByEmail(email))
                .add(
                        linkTo(methodOn(BossController.class).getBoss(email)).withSelfRel(),
                        linkTo(methodOn(BossController.class).updateBoss(null, null)).withRel("updateBoss"),
                        linkTo(methodOn(BossController.class).withdrawBoss(null)).withRel("withdrawBoss")
                );
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PatchMapping
    public ResponseEntity<BossResponseDTO> updateBoss(@AuthenticationPrincipal String email, @Valid @RequestBody BossUpdateRequestDTO reqeustDTO) {
        BossResponseDTO responseDTO = bossService.update(email, reqeustDTO)
                .add(
                        linkTo(methodOn(BossController.class).updateBoss(null, null)).withSelfRel(),
                        linkTo(methodOn(BossController.class).getBoss(email)).withRel("getBoss"),
                        linkTo(methodOn(BossController.class).withdrawBoss(null)).withRel("withdrawBoss")
                );

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody BossPasswordUpdateReqeuestDTO reqeuestDTO){
        bossService.updatePassword(reqeuestDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/find-email")
    public ResponseEntity<BossFindEmailResponseDTO> findEmail(@Valid @RequestBody BossFindEmailReqeustDTO reqeustDTO){
        BossFindEmailResponseDTO responseDTO = bossService.findEmailByPhoneNumber(reqeustDTO)
                .add(linkTo(methodOn(BossController.class).findEmail(null)).withSelfRel(),
                        linkTo(methodOn(BossController.class).updatePassword(null)).withRel("updatePassword"));
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping("/register-account")
    public ResponseEntity<BossResponseDTO> registerAccount(@AuthenticationPrincipal String email, @Valid @RequestBody BossRegisterAccountRequestDTO dto){
        BossResponseDTO responseDTO = bossService.registerAccount(email, dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}

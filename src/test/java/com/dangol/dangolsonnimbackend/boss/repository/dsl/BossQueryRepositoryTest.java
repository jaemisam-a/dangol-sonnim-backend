package com.dangol.dangolsonnimbackend.boss.repository.dsl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class BossQueryRepositoryTest {

    @Autowired
    private BossRepository bossRepository;
    @Autowired
    private BossQueryRepository bossQueryRepository;

    @BeforeEach
    public void setup() {

        BossSignupRequestDTO bossSignupRequestDTO = new BossSignupRequestDTO();
        bossSignupRequestDTO.setName("Test");
        bossSignupRequestDTO.setPassword("password");
        bossSignupRequestDTO.setEmail("test@test.com");
        bossSignupRequestDTO.setBossPhoneNumber("01012345678");
        bossSignupRequestDTO.setStoreRegisterNumber("1234567890");
        bossSignupRequestDTO.setStoreRegisterName("Test Store");
        bossSignupRequestDTO.setMarketingAgreement(true);

        bossRepository.save(new Boss(bossSignupRequestDTO));
    }

    @Test
    public void givenValidEmail_whenExistsByEmail_thenReturnTrue() {
        // given

        // when
        Boolean result = bossQueryRepository.existsByEmail("test@test.com");

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void givenValidEmail_whenExistsByEmail_thenReturnFalse() {
        // given

        // when
        Boolean result = bossQueryRepository.existsByEmail("invalid@email.com");

        // then
        assertThat(result).isFalse();
    }

    @Test
    void givenValidBpn_whenExistsByBpn_thenReturnTrue() {
        // given

        // when
        Boolean result = bossQueryRepository.existsByBpn("01012345678");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void givenValidBpn_whenExistsByBpn_thenReturnFalse() {
        // given

        // when
        Boolean result = bossQueryRepository.existsByEmail("010123456782");

        // then
        assertThat(result).isFalse();
    }

    @Test
    void givenValidSrn_whenExistsBySrn_thenReturnTrue() {
        // given

        // when
        Boolean result = bossQueryRepository.existsBySrn("1234567890");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void givenValidSrn_whenExistsBySrn_thenReturnFalse() {
        // given

        // when
        Boolean result = bossQueryRepository.existsBySrn("1111");

        // then
        assertThat(result).isFalse();
    }
}
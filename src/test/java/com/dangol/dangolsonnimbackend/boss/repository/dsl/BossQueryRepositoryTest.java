package com.dangol.dangolsonnimbackend.boss.repository.dsl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class BossQueryRepositoryTest {
    @Autowired
    private BossQueryRepository bossQueryRepository;
    @Autowired
    private BossRepository bossRepository;

    @BeforeEach
    public void setup() {
        bossRepository.deleteAll();
        BossSignupRequestDTO bossSignupRequestDTO = new BossSignupRequestDTO();
        bossSignupRequestDTO.setName("Test");
        bossSignupRequestDTO.setPassword("password");
        bossSignupRequestDTO.setEmail("test@test.com");
        bossSignupRequestDTO.setBossPhoneNumber("1234567890");
        bossSignupRequestDTO.setStoreRegisterNumber("1234567890");
        bossSignupRequestDTO.setStoreRegisterName("Test Store");
        bossSignupRequestDTO.setMarketingAgreement(true);
        bossRepository.save(new Boss(bossSignupRequestDTO));
    }

    @Test
    public void givenExistingEmail_whenCheckingIfExists_thenShouldReturnTrue() {
        // given
        String email = "test@test.com";

        // when
        boolean exists = bossQueryRepository.existsByEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void givenNonExistingEmail_whenCheckingIfExists_thenShouldReturnFalse() {
        // given
        String email = "non-existing@email.com";

        // when
        boolean exists = bossQueryRepository.existsByEmail(email);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    public void givenExistingBossPhoneNumber_whenCheckingIfExists_thenShouldReturnTrue() {
        // given
        String bossPhoneNumber = "1234567890";

        // when
        boolean exists = bossQueryRepository.existsByBpn(bossPhoneNumber);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void givenNonExistingBossPhoneNumber_whenCheckingIfExists_thenShouldReturnFalse() {
        // given
        String bossPhoneNumber = "0987654321";

        // when
        boolean exists = bossQueryRepository.existsByBpn(bossPhoneNumber);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    public void givenExistingStoreRegisterNumber_whenCheckingIfExists_thenShouldReturnTrue() {
        // given
        String srn = "1234567890";

        // when
        boolean exists = bossQueryRepository.existsBySrn(srn);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void givenNonExistingStoreRegisterNumber_whenCheckingIfExists_thenShouldReturnFalse() {
        // given
        String srn = "654321";

        // when
        boolean exists = bossQueryRepository.existsBySrn(srn);

        // then
        assertThat(exists).isFalse();
    }
}
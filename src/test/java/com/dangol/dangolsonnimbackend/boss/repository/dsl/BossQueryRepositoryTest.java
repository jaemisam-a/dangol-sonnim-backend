package com.dangol.dangolsonnimbackend.boss.repository.dsl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.dto.request.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.repository.BossRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        bossSignupRequestDTO.setPhoneNumber("01012345678");
        bossSignupRequestDTO.setMarketingAgreement(true);

        bossRepository.save(new Boss(bossSignupRequestDTO));
    }

    @Test
    void givenValidPhoneNumber_whenExistsByPhoneNumber_thenReturnTrue() {
        // given

        // when
        Boolean result = bossQueryRepository.existsByPhoneNumber("01012345678");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void givenValidPhoneNumber_whenExistsByPhoneNumber_thenReturnFalse() {
        // given

        // when
        Boolean result = bossQueryRepository.existsByPhoneNumber("1111");

        // then
        assertThat(result).isFalse();
    }

    @Test
    void givenValidEmail_whenExistsByEmail_thenReturnTrue() {
        // given

        // when
        Boolean result = bossQueryRepository.existsByEmail("test@test.com");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void givenValidEmail_whenExistsByEmail_thenReturnFalse() {
        // given

        // when
        Boolean result = bossQueryRepository.existsByPhoneNumber("1111");

        // then
        assertThat(result).isFalse();
    }

    @Test
    void givenSignupDto_whenFindByEmail_thenReturnBoss() {
        // given

        // when
        Boss boss = bossQueryRepository.findByEmail("test@test.com");

        // then
        assertNotNull(boss);
    }

    @Test
    void givenSignupDto_whenFindByPhoneNumber_thenReturnBoss() {
        // given

        // when
        Boss boss = bossQueryRepository.findByEmail("test@test.com");

        // then
        assertNotNull(boss);
    }
}

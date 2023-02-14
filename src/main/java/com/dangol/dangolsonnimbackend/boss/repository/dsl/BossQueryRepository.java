package com.dangol.dangolsonnimbackend.boss.repository.dsl;

import com.dangol.dangolsonnimbackend.boss.domain.QBoss;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BossQueryRepository {

    private final JPAQueryFactory queryFactory;

    public boolean existsByEmail(String email) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QBoss.boss)
                .where(QBoss.boss.email.eq(email))
                .fetchFirst();

        return fetchOne != null;
    }

    public boolean existsByBpn(String bossPhoneNumber) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QBoss.boss)
                .where(QBoss.boss.bossPhoneNumber.eq(bossPhoneNumber))
                .fetchFirst();

        return fetchOne != null;
    }

    public boolean existsBySrn(String srn) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QBoss.boss)
                .where(QBoss.boss.storeRegisterNumber.eq(srn))
                .fetchFirst();

        return fetchOne != null;
    }
}

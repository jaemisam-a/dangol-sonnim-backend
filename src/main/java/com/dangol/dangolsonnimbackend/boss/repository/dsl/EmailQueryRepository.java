package com.dangol.dangolsonnimbackend.boss.repository.dsl;

import com.dangol.dangolsonnimbackend.boss.domain.Email;
import com.dangol.dangolsonnimbackend.boss.domain.QEmail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class EmailQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Optional<Email> findByEmail(String email){
        return Optional.ofNullable(queryFactory.selectFrom(QEmail.email)
                .where(QEmail.email.bossEmail.eq(email))
                .fetchOne());
    }

    public boolean existsByEmailAndAuthCode(String email, String authCode){
        Integer fetchOne = queryFactory.selectOne()
                .from(QEmail.email)
                .where(QEmail.email.bossEmail.eq(email)
                        .and(QEmail.email.authCode.eq(authCode)))
                .fetchFirst();

        return fetchOne != null;
    }
}

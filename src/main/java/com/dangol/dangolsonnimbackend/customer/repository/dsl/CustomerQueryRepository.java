package com.dangol.dangolsonnimbackend.customer.repository.dsl;

import com.dangol.dangolsonnimbackend.customer.domain.QCustomer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerQueryRepository {

    private final JPAQueryFactory queryFactory;

    public boolean existsByNickname(String nickname) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QCustomer.customer)
                .where(QCustomer.customer.nickname.eq(nickname))
                .fetchFirst();

        return fetchOne != null;
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QCustomer.customer)
                .where(QCustomer.customer.phoneNumber.eq(phoneNumber))
                .fetchFirst();

        return fetchOne != null;
    }
}

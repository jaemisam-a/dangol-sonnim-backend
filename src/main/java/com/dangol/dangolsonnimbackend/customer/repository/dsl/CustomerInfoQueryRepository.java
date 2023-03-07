package com.dangol.dangolsonnimbackend.customer.repository.dsl;

import com.dangol.dangolsonnimbackend.customer.domain.QCustomerInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerInfoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public boolean existsByNickname(String nickname) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QCustomerInfo.customerInfo)
                .where(QCustomerInfo.customerInfo.nickname.eq(nickname))
                .fetchFirst();

        return fetchOne != null;
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QCustomerInfo.customerInfo)
                .where(QCustomerInfo.customerInfo.phoneNumber.eq(phoneNumber))
                .fetchFirst();

        return fetchOne != null;
    }
}

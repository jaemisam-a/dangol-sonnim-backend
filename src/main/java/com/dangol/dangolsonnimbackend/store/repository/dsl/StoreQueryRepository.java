package com.dangol.dangolsonnimbackend.store.repository.dsl;

import com.dangol.dangolsonnimbackend.boss.domain.QBoss;
import com.dangol.dangolsonnimbackend.store.domain.QStore;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 가게 사업자 번호에 대한 중복 체크를 한다.
     *
     * @param registerNumber 사업자 번호
     * @return ture or flase
     */
    public boolean existsBySrn(String registerNumber) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QStore.store)
                .where(QStore.store.storeRegisterNumber.eq(registerNumber))
                .fetchFirst();

        return fetchOne != null;
    }
}
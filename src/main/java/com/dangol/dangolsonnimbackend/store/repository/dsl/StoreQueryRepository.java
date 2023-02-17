package com.dangol.dangolsonnimbackend.store.repository.dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    // TODO. 중복체크 및 조회용 인터페이스 설계 필요 시 구현
}
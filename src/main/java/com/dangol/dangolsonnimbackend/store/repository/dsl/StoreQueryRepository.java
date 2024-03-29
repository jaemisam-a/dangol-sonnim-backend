package com.dangol.dangolsonnimbackend.store.repository.dsl;

import com.dangol.dangolsonnimbackend.boss.domain.QBoss;
import com.dangol.dangolsonnimbackend.store.domain.QCategory;
import com.dangol.dangolsonnimbackend.store.domain.QStore;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class StoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 가게 사업자 번호에 대한 중복 체크를 한다.
     *
     * @param registerNumber 사업자 번호
     * @return ture or false
     */
    public boolean existsByRegisterNumber(String registerNumber) {
        Integer fetchOne = queryFactory.selectOne()
                .from(QStore.store)
                .where(QStore.store.registerNumber.eq(registerNumber))
                .fetchFirst();

        return fetchOne != null;
    }

    /**
     * 가게 ID 값을 통해 가게 정보를 조회한다.
     *
     * @param id 가게 ID
     * @return 가게 엔티티 객체
     */
    public Optional<Store> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(QStore.store)
                .where(QStore.store.id.eq(id))
                .fetchOne());
    }

    /**
     * 가게 사업자 번호를 통해 가게 정보를 조회한다.
     *
     * @param registerNumber 가게 사업자 번호
     * @return 가게 엔티티 객체
     */
    public Optional<Store> findByRegisterNumber(String registerNumber) {
        return Optional.ofNullable(
                queryFactory.selectFrom(QStore.store)
                .where(QStore.store.registerNumber.eq(registerNumber))
                .fetchOne());
    }

    public Optional<List<Store>> findMyStore(String email) {
        return Optional.ofNullable(
                queryFactory.selectFrom(QStore.store)
                        .leftJoin(QStore.store.boss, QBoss.boss).fetchJoin()
                        .where(QBoss.boss.email.eq(email))
                        .fetch());
    }

    public List<Store> findBySigungu(String sigungu) {
        return queryFactory.selectFrom(QStore.store)
                .where(QStore.store.sigungu.eq(sigungu))
                .fetch();
    }

    public List<Store> findBySigunguAndCategoryType(String sigungu, CategoryType category) {
        return queryFactory.selectFrom(QStore.store)
                .leftJoin(QStore.store.category, QCategory.category).fetchJoin()
                .where(QStore.store.sigungu.eq(sigungu)
                        .and(QCategory.category.categoryType.eq(category)))
                .fetch();
    }
}
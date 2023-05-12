package com.dangol.dangolsonnimbackend.store.repository.dsl;

import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.domain.QCategory;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CategoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 카테고리 ID 값을 통해 카테고리 정보를 조회한다.
     *
     * @param id 카테고리 ID
     * @return 카테고리 엔티티 객체
     */
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.select(QCategory.category)
                        .where(QCategory.category.id.eq(id))
                        .fetchOne());
    }

    /**
     * 카테고리 코드 값을 통해 가게 목록을 조회한다.
     *
     * @param categoryType 카테고리 ID
     * @return 카테고리 엔티티 객체
     */
    public Category findByCategoryType(CategoryType categoryType) {
        return queryFactory.selectFrom(QCategory.category)
                        .where(QCategory.category.categoryType.eq(categoryType))
                        .fetchOne();
    }
}

package com.dangol.dangolsonnimbackend.store.repository.dsl;

import com.dangol.dangolsonnimbackend.store.domain.QTag;
import com.dangol.dangolsonnimbackend.store.domain.Tag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TagQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Tag> findByName(String name) {
        return Optional.ofNullable(
                queryFactory.selectFrom(QTag.tag)
                        .where(QTag.tag.name.eq(name))
                        .fetchOne());
    }

}

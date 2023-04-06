package com.dangol.dangolsonnimbackend.store.repository.dsl;

import com.dangol.dangolsonnimbackend.store.domain.Menu;
import com.dangol.dangolsonnimbackend.store.domain.QMenu;
import com.dangol.dangolsonnimbackend.store.domain.QStore;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MenuQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Optional<Menu> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(QMenu.menu)
                        .where(QMenu.menu.id.eq(id))
                        .fetchOne());
    }
}

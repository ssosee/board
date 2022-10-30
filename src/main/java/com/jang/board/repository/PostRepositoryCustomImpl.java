package com.jang.board.repository;

import com.jang.board.domain.Post;
import com.jang.board.domain.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private QPost p = new QPost("p");

    public PostRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<Post> findPostsByTitleOrContentOrAuthor(Pageable pageable, String title, String content, String author) {

        List<Post> result = queryFactory
                .selectFrom(p)
                .where(titleContains(title), contentContains(content), authorContains(author))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long total = queryFactory
                .select(p.count())
                .from(p)
                .where(titleContains(title), contentContains(content), authorContains(author))
                .fetchOne();


        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? p.title.contains(title) : null;
    }

    private BooleanExpression contentContains(String content) {
        return content != null ? p.content.contains(content) : null;
    }

    private BooleanExpression authorContains(String author) {
        return author != null ? p.member.userId.contains(author) : null;
    }
}

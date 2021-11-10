package ru.parohodov.servicearticles.datasource.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.parohodov.servicearticles.datasource.entity.Article;
import ru.parohodov.servicearticles.service.dto.ArticleDto;

import java.util.Optional;

/**
 * @author Pavel Popov
 */
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {
    Optional<Article> findByTitle(String title);
}

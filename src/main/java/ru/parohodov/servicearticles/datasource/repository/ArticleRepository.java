package ru.parohodov.servicearticles.datasource.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.parohodov.servicearticles.datasource.entity.Article;

/**
 * @author Parohodov
 */
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {
}

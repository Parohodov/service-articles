package ru.parohodov.servicearticles.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.parohodov.servicearticles.exception.ArticleNotFoundException;
import ru.parohodov.servicearticles.service.dto.ArticleDto;
import ru.parohodov.servicearticles.datasource.entity.Article;
import ru.parohodov.servicearticles.datasource.repository.ArticleRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Parohodov
 *
 * TODO JPA Criteria
 * TODO Transactional
 */
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleDto> getAllArticles() {
        Iterable<Article> all = articleRepository.findAll();
        List<ArticleDto> articles = new ArrayList<>();
        all.forEach(a -> articles.add(ArticleDto.builder()
                .title(a.getTitle())
                .theme(a.getTheme())
                .build()));
        return articles;
    }

    public ArticleDto getArticleById(long id) {
        Optional<Article> result = articleRepository.findById(id);
        if (result.isPresent()) {
            // Get url, read file for content and put title and content in a Dto
            Article article = result.get();
            return ArticleDto.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .theme(article.getTheme())
                    .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                    .build();
        } else {
            throw new ArticleNotFoundException();
        }
    }

    @PostConstruct
    public void populateDataBase() {
        for (int i = 1; i <= 10; i++) {
            articleRepository.save(new Article(
                    "name" + i,
                    "path" + (i % 2 + 1),
                    (long) i,
                    "theme" + i
                    )
            );
        }
    }
}

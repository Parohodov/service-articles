package ru.parohodov.servicearticles.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.parohodov.servicearticles.exception.ArticleAlreadyExistsException;
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
 * TODO: JPA Criteria, filter
 * FIXME: Transactional
 */
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleDto> getAllArticles() {
        Iterable<Article> all = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "uploadDate"));
        List<ArticleDto> articles = new ArrayList<>();
        all.forEach(article -> articles.add(new ArticleDto(article)));
        return articles;
    }

    public ArticleDto getArticleById(long id) {
        Optional<Article> result = articleRepository.findById(id);
        if (result.isEmpty()) {
            throw new ArticleNotFoundException("Article not found");

        }
        // Get url, read file for content and put title and content in a Dto
        Article article = result.get();
        return new ArticleDto(article);
    }

    public ArticleDto saveArticle(ArticleDto newArticle) {
        Optional<Article> result = articleRepository.findByTitle(newArticle.getTitle());
        if (result.isPresent()) {
            throw new ArticleAlreadyExistsException("Article already exists: " + newArticle.getTitle());
        }
        // TODO: storage file here
        // TODO: save to DB here
        return new ArticleDto(articleRepository.save(newArticle.toEntity()));
    }

    public void deleteById(long id) {
        articleRepository.deleteById(id);
    }

    @PostConstruct
    public void populateDataBase() throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
//            Thread.sleep(1000);
            articleRepository.save(new Article(
                    String.format("Title %03d",  i),
                    "path",
                    String.format("Theme %03d",  i),
                    new java.util.Date().getTime()
                    )
            );
        }
    }
}

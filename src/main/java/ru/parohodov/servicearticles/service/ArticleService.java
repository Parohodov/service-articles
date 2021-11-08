package ru.parohodov.servicearticles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.parohodov.servicearticles.exception.ArticleAlreadyExistsException;
import ru.parohodov.servicearticles.exception.ArticleNotFoundException;
import ru.parohodov.servicearticles.service.dto.ArticleDto;
import ru.parohodov.servicearticles.datasource.entity.Article;
import ru.parohodov.servicearticles.datasource.repository.ArticleRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Parohodov
 *
 * TODO: JPA Criteria, filter
 * FIXME: Transactional
 */
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final FileStorageService storageFile;
    private final FileProcessService processFile;
    private final ArticleRepository articleRepository;

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

   public ArticleDto saveArticle(MultipartFile file) {
        // First storage the file (to be able to use ZipFile)
        Path storedFile = storageFile.store(file);

        ArticleDto articleDto = processFile.processFile(storedFile);

        Optional<Article> result = articleRepository.findByTitle(articleDto.getTitle());
        if (result.isPresent()) {
            storageFile.delete(storedFile);
            throw new ArticleAlreadyExistsException("Article already exists: " + articleDto.getTitle());
        }

        articleDto.setArchivePath(storedFile.toString());
        saveToDataBase(articleDto.toEntity());

        result = articleRepository.findByTitle(articleDto.getTitle());
        if (result.isEmpty()) {
            throw new ArticleNotFoundException("Something went wrong");
        }

        return new ArticleDto(result.get());
    }

    @Transactional
    void saveToDataBase(Article entity) {
        articleRepository.save(entity);
    }

    @Transactional
    public void deleteById(long id) {
        articleRepository.deleteById(id);
    }

    @PostConstruct
    public void populateDataBase() {
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

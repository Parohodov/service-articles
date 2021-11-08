package ru.parohodov.servicearticles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.parohodov.servicearticles.exception.ArticleAlreadyExistsException;
import ru.parohodov.servicearticles.exception.ArticleNotFoundException;
import ru.parohodov.servicearticles.exception.FileFormatException;
import ru.parohodov.servicearticles.exception.StorageException;
import ru.parohodov.servicearticles.service.dto.ArticleDto;
import ru.parohodov.servicearticles.datasource.entity.Article;
import ru.parohodov.servicearticles.datasource.repository.ArticleRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Parohodov
 *
 * As long as H2 works runtime all data is retrieved from file system
 * while database keeps id's for a list, files url's and subject
 * It works because all data needed can be retrieved from file properies
 *
 * TODO: JPA Criteria, filter
 * FIXME: Transactional
 */
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final FileStorageService storageService;
    private final FileProcessService fileProcessService;
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
        ArticleDto articleDto = fileProcessService.readFile(Paths.get(article.getArchivePath()));
        articleDto.setId(id); // It needs id this time
        return articleDto;
    }

    public ArticleDto saveArticle(MultipartFile file) {
        // First storage the file (to be able to use ZipFile)
        Path storedFile;

        storedFile = storageService.store(file);

        ArticleDto articleDto = fileProcessService.readFile(storedFile);

        Optional<Article> result = articleRepository.findByTitle(articleDto.getTitle());
        if (result.isPresent()) {
            storageService.delete(storedFile);
            throw new ArticleAlreadyExistsException("Article already exists: " + articleDto.getTitle());
        }
        saveToDataBase(articleDto.toEntity());

        result = articleRepository.findByTitle(articleDto.getTitle());
        if (result.isEmpty()) {
            throw new ArticleNotFoundException("Something went wrong");
        }

        return fileProcessService.readFile(Paths.get(result.get().getArchivePath()));
    }

    @Transactional
    void saveToDataBase(Article entity) {
        articleRepository.save(entity);
    }

    @Transactional
    public void deleteById(long id) {
        Optional<Article> result = articleRepository.findById(id);
        if (result.isPresent()) {
            Path path = Paths.get(result.get().getArchivePath());
            storageService.delete(path);
            articleRepository.deleteById(id);
        }
    }

    @PostConstruct
    private void populateDataBase() {
        List<Path> files = storageService.getAllFiles();
        for (Path file : files) {
            ArticleDto articleDto = fileProcessService.readFile(file);
            articleRepository.save(articleDto.toEntity());
        }
    }
}

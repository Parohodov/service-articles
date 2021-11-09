package ru.parohodov.servicearticles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.parohodov.servicearticles.exception.*;
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
 * A service that works with a database
 *
 * As long as H2 works runtime, all data is retrieved from a file system,
 * while database keeps id's for a list, files url's and subjects.
 * That's why file system operations are used from here.
 *
 * TODO: filter, JPA Criteria, pagination
 */
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final FileStorageService storageService;
    private final FileProcessService fileProcessService;
    private final ArticleRepository articleRepository;

    public List<ArticleDto> getAllArticles() {
        Iterable<Article> allArticles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "uploadDate"));
        List<ArticleDto> allDto = new ArrayList<>();
        allArticles.forEach(article -> allDto.add(
                getDto(
                        article.getId(),
                        Paths.get(article.getArchivePath())
                )
        ));
        return allDto;
    }

    public ArticleDto getArticleById(long id) {
        Optional<Article> result = articleRepository.findById(id);
        if (result.isEmpty()) {
            throw new FileMissingException("Such an article doesn't exist");

        }
        Article article = result.get();
        return getDto(article.getId(), Paths.get(article.getArchivePath()));
    }

    @Transactional
    public ArticleDto saveArticle(MultipartFile file) {
        Path storedFile = storageService.store(file); // First storage the file (to be able to use ZipFile)

        ArticleDto dto = getDto(0, storedFile);

        Optional<Article> result = articleRepository.findByTitle(dto.getTitle());
        if (result.isPresent()) {
            // If running comes here than store(file) didn't threw an exception => file with such name doesn't exist
            // But an article with such article name does
            storageService.delete(storedFile);
            throw new FileFormatException("Article with such title already exists.");
        }

        Article freshArticle = articleRepository.save(dto.toEntity());
        dto.setId(freshArticle.getId());
        return dto;
    }

    @Transactional
    public void deleteById(long id) {
        Optional<Article> result = articleRepository.findById(id);
        if (result.isPresent()) {
            Path path = Paths.get(result.get().getArchivePath());
            // TODO: handling exceptions
            storageService.delete(path);
            articleRepository.deleteById(id);
        }
    }

    // Returned DTO's id is zero now
    private ArticleDto getDto(long articleId, Path storedFile) {
        ArticleDto dto;
        try {
            dto = fileProcessService.readFile(storedFile);
        } catch (FileFormatException e) {
            storageService.delete(storedFile);
            throw new FileFormatException(e.getMessage());
        } catch (FileCommonException e) {
            storageService.delete(storedFile);
            throw new FileCommonException(e.getMessage());
        } // FIXME: DRY violation in catch blocks

        dto.setId(articleId);
        return dto;
    }

    @PostConstruct
    private void populateDataBase() {
        Path articleDirectory = storageService.creatDirectory(null);
        List<Path> files = storageService.getAllFilePath(articleDirectory);
        for (Path file : files) {
            ArticleDto articleDto;
            try {
                articleDto = fileProcessService.readFile(file);
            } catch (FileFormatException | FileConflictException e) {
                continue; // Do not add file if something goes wrong
            }
            articleRepository.save(articleDto.toEntity());
        }
    }
}

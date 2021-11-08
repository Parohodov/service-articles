package ru.parohodov.servicearticles.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.parohodov.servicearticles.config.StorageProperties;
import ru.parohodov.servicearticles.datasource.entity.Article;
import ru.parohodov.servicearticles.exception.StorageException;
import ru.parohodov.servicearticles.service.dto.ArticleDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

/**
 * @author Parohodov
 */
@Service
class FileStorageService {
    private final Path rootLocation;

    public FileStorageService(StorageProperties properties) {
        rootLocation = Paths.get(properties.getLocation());
    }

    public ArticleDto store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("File is empty");
        }
        Path destinationFile = this.rootLocation.resolve(
                Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            // This is a security check
            throw new StorageException("Cannot store file outside current directory.");
        }

        ArticleDto articleDto;
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);

            articleDto = ArticleDto.builder()
                    .title("title")
                    .archivePath(destinationFile.toString())
                    .subject("subject")
                    .uploadDate(new Date())
                    .build();
        } catch (IOException e) {
            throw new StorageException("Cannot store file outside current directory.");
        }
        return articleDto;
    }
}

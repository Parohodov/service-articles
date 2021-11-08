package ru.parohodov.servicearticles.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.parohodov.servicearticles.config.StorageProperties;
import ru.parohodov.servicearticles.exception.StorageException;
import ru.parohodov.servicearticles.service.dto.ArticleDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * @author Parohodov
 */
@Service
public class FileStorageService {
    private final Path rootLocation;

    public FileStorageService(StorageProperties properties) {
        rootLocation = Paths.get(properties.getLocation());
    }

    public Path store(MultipartFile file) {
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
        } catch (IOException e) {
            throw new StorageException("Cannot store file outside current directory.");
        }
        return destinationFile;
    }

    public void delete(Path file) {
        try {
            Files.delete(file);
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", file);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", file);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }
}

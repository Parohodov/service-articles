package ru.parohodov.servicearticles.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.parohodov.servicearticles.config.StorageProperties;
import ru.parohodov.servicearticles.exception.FileFormatException;
import ru.parohodov.servicearticles.exception.StorageException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Parohodov
 *
 * This service stores files on disc in rootLocation inside project root directory.
 */
@Service
public class FileStorageService {
    private final Path rootLocation;

    public FileStorageService(StorageProperties properties) {
        rootLocation = Paths.get(properties.getLocation());
    }

    public Path store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Zip file is empty.");
        }

        Path destinationFile = this.rootLocation.resolve(
                Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();

        String fileExtension = FilenameUtils.getExtension(destinationFile.toString());
        if (!fileExtension.equals("zip")) {
            throw new StorageException("File is not a Zip.");
        }

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            // This is a security check
            throw new StorageException("Cannot store zip file outside current directory.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile);
        } catch (FileAlreadyExistsException e) {
            throw new StorageException("Zip file with such name already exists.");
        } catch (IOException e) {
            throw new StorageException("Failed to store zip file.");
        }
        return destinationFile;
    }

    public List<Path> getAllFiles() {
        List<Path> files;
        try (Stream<Path> paths = Files.walk(this.rootLocation)) {
            files = paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Failed to get all files");
        }
        return files;
    }

    public void delete(Path file) {
        try {
            Files.delete(file);
        } catch (NoSuchFileException x) {
            throw new StorageException("File not found");
        } catch (IOException x) {
            // File permission problems are caught here.
            throw new StorageException("Something went wrong.");
        }
    }
}

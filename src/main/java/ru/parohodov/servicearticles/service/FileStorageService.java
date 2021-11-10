package ru.parohodov.servicearticles.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.parohodov.servicearticles.config.StorageProperties;
import ru.parohodov.servicearticles.exception.FileConflictException;
import ru.parohodov.servicearticles.exception.FileFormatException;
import ru.parohodov.servicearticles.exception.FileMissingException;
import ru.parohodov.servicearticles.exception.FileCommonException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Popov Pavel
 *
 * This service stores files on disc in rootLocation inside project root directory.
 */
@Service
public class FileStorageService {
    private final Path rootLocation;

    public FileStorageService(StorageProperties properties) {
        rootLocation = Paths.get(properties.getLocation());
    }

    /** Stores a given file
     * @param file - a file to be stored
     * @return - a Path to a file stored in a file system
     * @throws - FileMissingException, FileFormatException, FileConflictException, FileCommonException
     */
    public Path store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileMissingException("Zip file is missing.");
        }

        Path destinationFile = this.rootLocation.resolve(
                Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();

        String fileExtension = FilenameUtils.getExtension(destinationFile.toString());
        if (!fileExtension.equals("zip")) {
            throw new FileFormatException("File is not a Zip: " + file.getOriginalFilename());
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile);
        } catch (FileAlreadyExistsException e) {
            throw new FileConflictException("Zip file with such name already exists: " + file.getOriginalFilename());
        } catch (IOException e) {
            throw new FileCommonException("Failed to store zip file: " + file.getOriginalFilename());
        }
        return destinationFile;
    }

    /** Returns a list of file stored in a current storage defined by a rootLocation field
     * @return - List<Path> - list of files stored in a storage
     * @throws - FileCommonException
     */
    public List<Path> getAllFilePaths(Path directory) {
        List<Path> files;
        try (Stream<Path> paths = Files.walk(directory)) {
            files = paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileCommonException("Failed to get all files.");
        }
        return files;
    }

    /** Create directory from a Path passed in a parameter
     * @return - Path - list of files stored in a storage
     * @throws - FileCommonException
     */
    public Path creatDirectory(Path directoryName) {
        if (directoryName == null) {
            directoryName = rootLocation;
        }
        try {
            Files.createDirectory(directoryName);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Directory already exists: " + directoryName + ". " + e.getMessage());
        } catch (IOException e) {
            throw new FileCommonException("Failed to create directory");
        }
        return directoryName;
    }

    /**
     * Deletes given file
     * @throws - FileMissingException, FileCommonException
     */
    public void delete(Path file) {
        try {
            Files.delete(file);
        } catch (NoSuchFileException x) {
            throw new FileMissingException("File not found: " + file.getFileName());
        } catch (IOException x) {
            // File permission problems are caught here.
            throw new FileCommonException("Something went wrong.");
        }
    }
}

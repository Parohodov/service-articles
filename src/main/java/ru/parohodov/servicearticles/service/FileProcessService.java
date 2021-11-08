package ru.parohodov.servicearticles.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import ru.parohodov.servicearticles.config.StorageProperties;
import ru.parohodov.servicearticles.exception.FileFormatException;
import ru.parohodov.servicearticles.exception.StorageException;
import ru.parohodov.servicearticles.service.dto.ArticleDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Parohodov
 *
 * This service works with what is inside the archives
 */
@Service
public class FileProcessService {
    private final String predefinedFileName;

    public FileProcessService(StorageProperties properties) {
        predefinedFileName = properties.getArticleFileName();
    }

    public ArticleDto readFile(Path path) {

        List<String> lines = readZipFile(path);
        if (lines == null) {
            throw new FileFormatException("Something went wrong");
        }

        String title = lines.get(0);
        lines.remove(0);
        String content = lines.stream().collect(Collectors.joining(System.lineSeparator()));

        return ArticleDto.builder()
                .title(title)
                .archivePath(path.toString())
                .subject("?")
                .uploadDate(getFileCreationTime(path))
                .content(content)
                .build();
    }

    private List<String> readZipFile(Path path) {
        List<String> lines;

        try (ZipFile zipFile = new ZipFile(path.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            ZipEntry entry = entries.nextElement();
            if (entries.hasMoreElements()) {
                throw new FileFormatException("Wrong archive format");
            }

            if (!entry.getName().equals(predefinedFileName)) {
                throw new FileFormatException("Wrong archive format");
            }

            lines = readZipFileForLines(zipFile, entry);

            if (lines.size() == 0) {
                throw new FileFormatException("File is empty");
            }
            if (lines.size() == 1) {
                throw new FileFormatException("Body is missing");
            }
            // FIXME: Close entry?
        } catch (IOException e) {
            throw new FileFormatException("Can not open archive");
        }

        return lines;
    }

    private List<String> readZipFileForLines(ZipFile zipFile, ZipEntry entry) throws IOException {
        List<String> strings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                strings.add(line);
            }
        }
        return strings;
    }

    private Date getFileCreationTime(Path path) {
        BasicFileAttributes attr;
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            throw new StorageException("Failed to read file");
        }
        return new Date(attr.creationTime().toMillis());
    }
}

package ru.parohodov.servicearticles.service;

import org.springframework.stereotype.Service;
import ru.parohodov.servicearticles.config.StorageProperties;
import ru.parohodov.servicearticles.exception.FileFormatException;
import ru.parohodov.servicearticles.exception.FileCommonException;
import ru.parohodov.servicearticles.service.dto.ArticleDto;
import ru.parohodov.servicearticles.service.dto.ContentDto;

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

    /**
     * Reads a given PREVIOUSLY STORED file
     * @param path - a path to a stored file
     * @return - DTO with id field is zero
     * @throws - FileFormatException, FileCommonException
     */
    public ArticleDto readFile(Path path) {

        List<String> lines = readZipFile(path);
        String title = lines.get(0).replace(".", "");
        lines.remove(0);

        List<ContentDto> content = lines.stream().map(ContentDto::new).collect(Collectors.toList());

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
                throw new FileFormatException("Too much article files inside an archive: " + path.getFileName());
            }

            if (!entry.getName().equals(predefinedFileName)) {
                throw new FileFormatException("Wrong article file name: " + path.getFileName());
            }

            lines = readZipFileForLines(zipFile, entry);

            if (lines.size() == 0) {
                throw new FileFormatException("Article file is empty: " + path.getFileName());
            }
            if (lines.size() == 1) {
                throw new FileFormatException("Article body is missing, file: " + path.getFileName());
            }
            // FIXME: Close entry?
        } catch (IOException e) {
            throw new FileCommonException("Can not open archive");
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
            throw new FileCommonException("Failed to read file time creation");
        }
        return new Date(attr.creationTime().toMillis());
    }
}

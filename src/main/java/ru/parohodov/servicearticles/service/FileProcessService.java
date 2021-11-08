package ru.parohodov.servicearticles.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.parohodov.servicearticles.config.StorageProperties;
import ru.parohodov.servicearticles.exception.FileFormatException;
import ru.parohodov.servicearticles.exception.OpenFileFailedException;
import ru.parohodov.servicearticles.service.dto.ArticleDto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author Parohodov
 */
@Service
public class FileProcessService {
    private final String articleFileNameFormat;
    private final Path rooLocation;

    public FileProcessService(StorageProperties properties) {
        articleFileNameFormat = properties.getArticleFileName();
        rooLocation = Paths.get(properties.getLocation());
    }

    public ArticleDto processFile(Path path) {

        String fileExtension = FilenameUtils.getExtension(path.toString());
        if (!fileExtension.equals("zip")) {
            throw new FileFormatException("File is not a zip archive");
        }

        List<String> lines = readZipFile(path.toFile());

        if (lines == null) {
            throw new FileFormatException("Something went wrong");
        }
        return ArticleDto.builder()
                .title(lines.get(0))
                .uploadDate(new Date())
                .build();
    }

    private List<String> readZipFile(File file) {
        List<String> lines;

        try (ZipFile zipFile = new ZipFile(file)) {

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            ZipEntry entry = entries.nextElement();
            if (entries.hasMoreElements()) {
                throw new FileFormatException("Wrong archive format");
            }

            if (!entry.getName().equals(articleFileNameFormat)) {
                throw new FileFormatException("Wrong archive format");
            }

            lines = readZipFileForLines(zipFile, entry);

            if (lines.size() == 0) {
                throw new FileFormatException("File is empty");
            }
            if (lines.size() == 1) {
                throw new FileFormatException("Body is missing");
            }
            // TODO: Close entry?
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

    private List<String> readZip(MultipartFile file) throws OpenFileFailedException {
        List<String> lines;

        try (ZipInputStream zin = new ZipInputStream(file.getInputStream())) {

            ZipEntry entry = zin.getNextEntry();
            if (zin.getNextEntry() != null) {
                throw new FileFormatException("Wrong archive format");
            }
            if (entry.isDirectory()) {
                throw new FileFormatException("Wrong archive format");
            }
            if (!entry.getName().equals(articleFileNameFormat)) {
                throw new FileFormatException("Wrong archive format");
            }

            lines = readZipStreamForLines(zin);
            zin.closeEntry();

        } catch (IOException e) {
            throw new OpenFileFailedException("File can't be read");
        }
        return lines;
    }

    private List<String> readZipStreamForLines(ZipInputStream zin) throws IOException {
        List<String> strings = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(zin, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                strings.add(line);
            }
        }
        return strings;
    }

}

package ru.parohodov.servicearticles;

import org.junit.Before;
import org.junit.Test;
import ru.parohodov.servicearticles.config.StorageProperties;
import ru.parohodov.servicearticles.exception.FileCommonException;
import ru.parohodov.servicearticles.exception.FileFormatException;
import ru.parohodov.servicearticles.service.FileProcessService;
import ru.parohodov.servicearticles.service.dto.ArticleDto;
import ru.parohodov.servicearticles.service.dto.ContentDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Pavel Popov
 */
public class FileProcessServiceTests {
    private FileProcessService fileProcessService;
    ClassLoader classLoader;

    @Before
    public void setUp() {
        fileProcessService = new FileProcessService(new StorageProperties("articles", "article.txt"));
        classLoader = getClass().getClassLoader();
    }

    @Test
    public void testReadFile() throws IOException {
        Path path = Paths.get("test-articles/test.zip");
        Date date = new Date(Files.readAttributes(path, BasicFileAttributes.class).creationTime().toMillis());
        ArticleDto dto = fileProcessService.readFile(path);

        assert (dto.equals(
                ArticleDto.builder()
                        .id(0)
                        .title("Test title")
                        .archivePath(path.toString())
                        .subject("?")
                        .uploadDate(date)
                        .content(Arrays.asList(new ContentDto("test"), new ContentDto("test")))
                        .build()
        ));
    }

    @Test
    public void testTooManyArticlesException() {
        Path path = Paths.get("test-articles/wrong/too-many.zip");
        Exception e = assertThrows(FileFormatException.class, () -> fileProcessService.readFile(path));
        assertNotNull(e.getMessage());
    }

    @Test
    public void testWrongArticlesFileNameException() {
        Path path = Paths.get("test-articles/wrong/wrong-name.zip");
        Exception e = assertThrows(FileFormatException.class, () -> fileProcessService.readFile(path));
        assertNotNull(e.getMessage());
    }

    @Test
    public void testArticleFileIsEmptyException() {
        Path path = Paths.get("test-articles/wrong/empty.zip");
        Exception e = assertThrows(FileFormatException.class, () -> fileProcessService.readFile(path));
        assertNotNull(e.getMessage());
    }


    @Test
    public void testArticleBodyIsMissingException() {
        Path path = Paths.get("test-articles/wrong/nobody.zip");
        Exception e = assertThrows(FileFormatException.class, () -> fileProcessService.readFile(path));
        assertNotNull(e.getMessage());
    }


    @Test
    public void testOtherIoExceptions() {
        Path path = Paths.get("test-articles/wrong/blablabla123.zip");
        Exception e = assertThrows(FileCommonException.class, () -> fileProcessService.readFile(path));
        assertNotNull(e.getMessage());
    }

}

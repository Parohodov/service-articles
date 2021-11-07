package ru.parohodov.servicearticles.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.parohodov.servicearticles.datasource.entity.Article;

import java.util.Date;

/**
 * @author Parohodov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private long id;
    private String title;
    private String archivePath;
    private String subject;
    private Date uploadDate;
    private String content;

    public ArticleDto(Article entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.archivePath = entity.getArchivePath();
        this.subject = entity.getSubject();
        this.uploadDate = new Date(entity.getUploadDate());
        // TODO: load file content somehow
        this.content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    }

    public Article toEntity() {
        return new Article(
                this.title,
                this.archivePath,
                this.subject,
                this.uploadDate.getTime()
        );
    }
}

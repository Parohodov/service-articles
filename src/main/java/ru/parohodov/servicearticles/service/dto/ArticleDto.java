package ru.parohodov.servicearticles.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.parohodov.servicearticles.datasource.entity.Article;

import java.time.LocalDateTime;
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

    // This one is need to show work with list
    public ArticleDto(Article entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.uploadDate = new Date(entity.getUploadDate());
    }

    // FIXME: Use mapping
    public Article toEntity() {
        return new Article(
                this.title,
                this.archivePath,
                this.subject,
                this.uploadDate.getTime()
        );
    }
}

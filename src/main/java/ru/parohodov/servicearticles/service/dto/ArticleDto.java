package ru.parohodov.servicearticles.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.parohodov.servicearticles.datasource.entity.Article;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Pavel Popov
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
    private List<ContentDto> content;

    public Article toEntity() {
        return new Article(
                this.title,
                this.archivePath,
                this.subject,
                this.uploadDate.getTime()
        );
    }

    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        }

        if (this == otherObject) {
            return true;
        }

        if (!(otherObject instanceof ArticleDto)) {
            return false;
        }

        ArticleDto other = (ArticleDto) otherObject;

        return this.id == other.id
                && this.title.equals(other.getTitle())
                && this.archivePath.equals(other.getArchivePath())
                && this.subject.equals(other.getSubject())
                && this.uploadDate.getTime() == other.uploadDate.getTime()
                && this.content.equals(other.content);
    }
}

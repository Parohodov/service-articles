package ru.parohodov.servicearticles.datasource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author Parohodov
 */
@Data
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String archivePath;
    private Long authorId;
    private String theme;
//    private Date uploadDate;
    private long uploadDate;

    public Article(String title, String archivePath, Long authorId, String theme, long uploadTime) {
        this.title = title;
        this.archivePath = archivePath;
        this.authorId = authorId;
        this.theme = theme;
        this.uploadDate = uploadTime;
    }
}

package ru.parohodov.servicearticles.datasource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String subject;
//    private Date uploadDate;
    private long uploadDate;

    public Article(String title, String archivePath, Long authorId, String subject, long uploadTime) {
        this.title = title;
        this.archivePath = archivePath;
        this.authorId = authorId;
        this.subject = subject;
        this.uploadDate = uploadTime;
    }
}

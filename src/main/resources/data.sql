DROP TABLE IF EXISTS article;

CREATE TABLE article (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              title VARCHAR(250) NOT NULL,
                              archive_path VARCHAR(260) NOT NULL,
                              subject VARCHAR(50) NOT NULL,
                              uploadDate BIGINT
);
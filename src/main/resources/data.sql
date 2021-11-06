DROP TABLE IF EXISTS article;

CREATE TABLE article (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(250) NOT NULL,
                              archive_path VARCHAR(260) NOT NULL,
                              author_id BIGINT,
                              theme VARCHAR(50) NOT NULL,
                              upload_date TIME WITH TIME ZONE default CURRENT_TIME
);

INSERT INTO article (name, archive_path, author_id, theme) VALUES
('Eating food', 'C:\\', 1, 'food'),
('Drinking water', 'C:\\', 2, 'beverages'),
('Breathing air', 'C:\\', 3,  'essential');
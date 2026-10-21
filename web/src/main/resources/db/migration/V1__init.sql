CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

CREATE TABLE books (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       pub_year INT NOT NULL
);

CREATE TABLE comments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          book_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          text VARCHAR(1000) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_comment_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
                          CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Тестові дані (для +20% до оцінки)
INSERT INTO users (username, password, role) VALUES ('admin', 'secret', 'ROLE_ADMIN');
INSERT INTO users (username, password, role) VALUES ('john_doe', 'pass123', 'ROLE_USER');

INSERT INTO books (title, author, pub_year) VALUES ('Кобзар', 'Тарас Шевченко', 1840);
INSERT INTO books (title, author, pub_year) VALUES ('Тигролови', 'Іван Багряний', 1944);

INSERT INTO comments (book_id, user_id, text) VALUES (1, 2, 'Чудова книга, класика!');
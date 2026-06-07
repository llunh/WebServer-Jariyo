USE JariyoDB;

CREATE TABLE IF NOT EXISTS restaurants (
    id       INT          NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    address  VARCHAR(255),
    category VARCHAR(50),
    PRIMARY KEY (id)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS reviews (
    id             INT     NOT NULL AUTO_INCREMENT,
    user_id        INT     NOT NULL,
    restaurant_id  INT     NOT NULL,
    rating         TINYINT NOT NULL,
    content        TEXT    NOT NULL,
    created_at     DATETIME DEFAULT NOW(),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)       REFERENCES users(id)       ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS review_images (
    id         INT          NOT NULL AUTO_INCREMENT,
    review_id  INT          NOT NULL,
    file_name  VARCHAR(255) NOT NULL,
    ori_name   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

INSERT INTO restaurants (name, address, category, phone, opening_hours, is_active) VALUES
    ('맛있는 한식당', '서울시 강남구 테헤란로 1', '한식', '02-1234-5678', '09:00~21:00', 1),
    ('이탈리아 피자', '서울시 마포구 홍대로 5', '양식', '02-2345-6789', '11:00~22:00', 1),
    ('스시 오마카세', '서울시 서초구 반포대로 10', '일식', '02-3456-7890', '12:00~21:00', 1);
    
DESC restaurants;
    
USE JariyoDB;

SELECT id, username, nickname, email, created_at FROM users;

CREATE TABLE IF NOT EXISTS review_likes (
    id         INT NOT NULL AUTO_INCREMENT,
    review_id  INT NOT NULL,
    user_id    INT NOT NULL,
    created_at DATETIME DEFAULT NOW(),
    PRIMARY KEY (id),
    UNIQUE KEY unique_like (review_id, user_id),  -- 중복 좋아요 방지
    FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id)   REFERENCES users(id)   ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;
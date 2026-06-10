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


ALTER TABLE reviews
  ADD COLUMN reservation_id INT UNIQUE AFTER user_id,
  ADD FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS review_images (
    id         INT          NOT NULL AUTO_INCREMENT,
    review_id  INT          NOT NULL,
    file_name  VARCHAR(255) NOT NULL,
    ori_name   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS review_likes (
    id         INT NOT NULL AUTO_INCREMENT,
    review_id  INT NOT NULL,
    user_id    INT NOT NULL,
    created_at DATETIME DEFAULT NOW(),
    PRIMARY KEY (id),
    UNIQUE KEY unique_like (review_id, user_id),
    FOREIGN KEY (review_id) REFERENCES reviews(id)  ON DELETE CASCADE,
    FOREIGN KEY (user_id)   REFERENCES users(id)    ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

show tables;
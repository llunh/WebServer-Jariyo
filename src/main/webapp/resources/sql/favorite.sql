USE JariyoDB;

CREATE TABLE IF NOT EXISTS favorites (
    id            INT NOT NULL AUTO_INCREMENT,
    user_id       INT NOT NULL,
    restaurant_id INT NOT NULL,
    created_at    DATETIME DEFAULT NOW(),
    PRIMARY KEY (id),
    UNIQUE KEY unique_favorite (user_id, restaurant_id),
    FOREIGN KEY (user_id)       REFERENCES users(id)       ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;
CREATE TABLE menus (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id   INT NOT NULL,
    name            VARCHAR(100) NOT NULL,
    price           INT NOT NULL,
    description     VARCHAR(255),
    category        VARCHAR(50),
    image_filename  VARCHAR(255),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

desc menus;
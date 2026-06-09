CREATE TABLE restaurants (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    address  VARCHAR(255) NOT NULL,
    category VARCHAR(50)
);

ALTER TABLE restaurants add column phone varchar(20) not null;
ALTER TABLE restaurants add column opening_hours varchar(100) not null;
ALTER TABLE restaurants add column is_active boolean not null;

ALTER TABLE restaurants ADD COLUMN image_filename VARCHAR(255);
ALTER TABLE restaurants ADD COLUMN max_capacity INT NOT NULL DEFAULT 10;
desc restaurants;

desc restaurants;

USE JariyoDB;
e5f56e2e108b859293194f3b38545cce8334cb1f

ALTER TABLE restaurants ADD COLUMN max_party_size INT NOT NULL DEFAULT 1;
CREATE TABLE restaurants (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    address  VARCHAR(255) NOT NULL,
    category VARCHAR(50)
);

ALTER TABLE restaurants add column phone varchar(20) not null;
ALTER TABLE restaurants add column opening_hours varchar(100) not null;
ALTER TABLE restaurants add column is_active boolean not null;

desc restaurants;
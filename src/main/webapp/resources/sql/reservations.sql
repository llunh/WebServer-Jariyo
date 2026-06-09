create table reservations (
	  id                INT AUTO_INCREMENT PRIMARY KEY,
    user_id           INT NOT NULL,
    restaurant_id     INT NOT NULL,
    reservation_date  DATE NOT NULL,
    reservation_time  TIME NOT NULL,
    party_size        INT NOT NULL,  -- 예약 인원 수
    status            ENUM('CONFIRMED', 'CANCELLED') DEFAULT 'CONFIRMED',
    created_at        DATETIME DEFAULT NOW(),

    FOREIGN KEY (user_id)       REFERENCES users(id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

ALTER TABLE reservations MODIFY user_id INT NULL;

desc reservations;



select * from reservations;
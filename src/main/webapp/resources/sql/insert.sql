INSERT INTO restaurants (name, address, category, phone, opening_hours, is_active, max_capacity, image_filename)
VALUES
('한우마을', '서울시 강남구 테헤란로 123', '한식', '02-1234-5678', '11:00 - 22:00', true, 5, 'restaurant1.jpg'),
('스시히로', '서울시 마포구 홍대입구로 45', '일식', '02-2345-6789', '12:00 - 21:00', true, 4, 'restaurant2.jpg'),
('파스타노', '서울시 용산구 이태원로 67', '양식', '02-3456-7890', '11:30 - 21:30', true, 6, 'restaurant3.jpg');


INSERT INTO restaurants (name, address, category, phone, opening_hours, is_active, max_capacity, image_filename)
VALUES
('북경반점', '서울시 중구 명동길 89', '중식', '02-4567-8901', '10:00 - 21:00', true, 8, 'restaurant4.jpg'),
('바베큐킹', '서울시 송파구 잠실로 34', '한식', '02-5678-9012', '12:00 - 23:00', true, 6, 'restaurant5.jpg'),
('타코벨라', '서울시 강서구 공항대로 56', '멕시코식', '02-6789-0123', '11:00 - 22:00', true, 5, 'restaurant6.jpg');

UPDATE restaurants SET name = '보글보글' WHERE name = '바베큐킹';
UPDATE restaurants SET category = '기타' WHERE category = '멕시코식';

select *from restaurants;


INSERT INTO menus (restaurant_id, name, price, description, category, image_filename)
VALUES
(1, '등심(170g)', 48000, '기름진 야들야들 등심', '메인', 'r1_menu1.jpg'),
(1, '안심(170g)', 55000, '담백하고 부드러운 안심', '메인', 'r1_menu2.jpg'),
(1, '채끝(170g)',52000, '풍부하고 부드러운 채끝', '메인', 'r1_menu3.jpg');
INSERT INTO menus (restaurant_id, name, price, description, category, image_filename)
VALUES
(1, '물냉면', 7000, '시원한~ 물냉면', '사이드', 'r1_menu4.jpg'),
(1, '비빔냉면',7000, '새콤달콤 비냉', '사이드', 'r1_menu5.jpg');

INSERT INTO menus (restaurant_id, name, price, description, category, image_filename)
VALUES
(2, '모둠초밥', 16000, '12p', '메인', 'r2_menu1.jpg'),
(2, '광어초밥',11000, '10p', '메인', 'r2_menu2.jpg'),
(2, '연어초밥',11000, '10p', '메인', 'r2_menu3.jpg'),
(2, '새우초밥',11000, '10p', '메인', 'r2_menu5.jpg');
INSERT INTO menus (restaurant_id, name, price, category, image_filename)
VALUES
(2, '우동', 6000, '사이드', 'r2_menu4.jpg'),
(2, '냉모밀',7000,  '사이드', 'r2_menu6.jpg');

select * from menus;

INSERT INTO menus (restaurant_id, name, price, description, category, image_filename)
VALUES
(3, '까르보나라 파스타', 12000, '이탈리아 정통', '메인', 'r3_menu1.jpg'),
(3, '토마토 파스타',11000, '우리 가게 대표 메뉴','메인', 'r3_menu2.jpg'),
(3, '봉골레 파스타',13000, '고소한 풍미','메인', 'r3_menu3.webp'),
(3, '마르게리따 피자',21000,  '화덕피자','메인', 'r3_menu4.jpg'),
(3, '고르곤졸라 피자',20000,  '화덕피자','메인', 'r3_menu4.jpg');


INSERT INTO menus (restaurant_id, name, price, category, image_filename)
VALUES
(4, '짜장면', 8000,  '메인', 'r4_menu1.jpg'),
(4, '짬뽕',10000, '메인', 'r4_menu2.jpg'),
(4, '탕수육',18000, '메인', 'r4_menu3.webp');

INSERT INTO menus (restaurant_id, name, price, category, image_filename)
VALUES
(5, '김치찌개 1인분', 8000,  '메인', 'r5_menu1.jpg'),
(5, '된장찌개 1인분',8000, '메인', 'r5_menu2.jpg'),
(5, '공깃밥',1000, '메인', 'r5_menu3.jpg');

INSERT INTO menus (restaurant_id, name, price, category, image_filename)
VALUES
(6, '치킨타코', 8500,  '메인', 'r6_menu1.jpg'),
(6, '폴드포크 타코',9000, '메인', 'r6_menu2.jpeg'),
(6, '쉬림프 타코',9500, '메인', 'r6_menu3.jpg');




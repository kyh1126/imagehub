-- 사용자 데이터 추가 (admin 계정)
INSERT INTO users (id, user_id, password, name, role, created_at, updated_at)
VALUES (1, 'admin', '{bcrypt}$2a$10$bINCxBcxYjS/R9SpDC8HP.w2wbucc2khp/jLV73eUt8CSeZQRWZKu', '관리자', 'ADMIN', now(),
        now());

-- 이미지 데이터 추가 (file_path와 thumbnail_path 필수)
INSERT INTO images (id, file_name, description, file_path, thumbnail_path, created_at, updated_at)
VALUES (1, 'image1.jpg', 'First image', 'uploads/image1.jpg', 'thumbnails/thumb_image1.jpg', now(), now());
INSERT INTO images (id, file_name, description, file_path, thumbnail_path, created_at, updated_at)
VALUES (2, 'image2.jpg', 'Second image', 'uploads/image2.jpg', 'thumbnails/thumb_image2.jpg', now(), now());
INSERT INTO images (id, file_name, description, file_path, thumbnail_path, created_at, updated_at)
VALUES (3, 'image3.jpg', 'Third image', 'uploads/image3.jpg', 'thumbnails/thumb_image3.jpg', now(), now());

-- 카테고리 데이터 추가
INSERT INTO categories (id, name, created_at, updated_at)
VALUES (1, 'PERSON', now(), now());
INSERT INTO categories (id, name, created_at, updated_at)
VALUES (2, 'LANDSCAPE', now(), now());
INSERT INTO categories (id, name, created_at, updated_at)
VALUES (3, 'ANIMAL', now(), now());
INSERT INTO categories (id, name, created_at, updated_at)
VALUES (4, 'FOOD', now(), now());
INSERT INTO categories (id, name, created_at, updated_at)
VALUES (5, 'OTHERS', now(), now());

-- 이미지와 카테고리 매핑
INSERT INTO image_categories (image_id, category)
VALUES (1, 'PERSON');
INSERT INTO image_categories (image_id, category)
VALUES (1, 'ANIMAL');
INSERT INTO image_categories (image_id, category)
VALUES (2, 'LANDSCAPE');
INSERT INTO image_categories (image_id, category)
VALUES (3, 'FOOD');

-- 시퀀스 값 조정
-- ALTER SEQUENCE users_id_seq RESTART WITH 2;
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('images_id_seq', (SELECT MAX(id) FROM images));
SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories));

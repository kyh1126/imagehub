INSERT INTO users (userId, password, name, role)
VALUES ('admin', 'password', '관리자', 'ROLE_ADMIN');

INSERT INTO images (id, file_name, description)
VALUES (1, 'image1.jpg', 'First image');
INSERT INTO images (id, file_name, description)
VALUES (2, 'image2.jpg', 'Second image');
INSERT INTO images (id, file_name, description)
VALUES (3, 'image3.jpg', 'Third image');

INSERT INTO categories (id, name)
VALUES (1, 'PERSON');
INSERT INTO categories (id, name)
VALUES (2, 'LANDSCAPE');
INSERT INTO categories (id, name)
VALUES (3, 'ANIMAL');
INSERT INTO categories (id, name)
VALUES (4, 'FOOD');
INSERT INTO categories (id, name)
VALUES (5, 'OTHERS');

INSERT INTO image_categories (image_id, category)
VALUES (1, 'PERSON');
INSERT INTO image_categories (image_id, category)
VALUES (1, 'ANIMAL');
INSERT INTO image_categories (image_id, category)
VALUES (2, 'LANDSCAPE');
INSERT INTO image_categories (image_id, category)
VALUES (3, 'FOOD');

INSERT INTO "orders"."customer" ("first_name", "last_name", "middle_name", "login")
VALUES
('Имя1', 'Фамилия1', NULL, 'user1'),
('Имя2', 'Фамилия2', 'Отчество', 'user2'),
('Имя3', 'Фамилия3', 'Отчество3', 'login')
;

INSERT INTO "orders"."order" ("customer_id", "status")
VALUES
(1, 'PROCESSING'),
(1, 'CLOSED'),
(1, 'PROCESSING'),
(2, 'PROCESSING'),
(2, 'CLOSED'),
(1,'PROCESSING'),
(1,'CANCELED')
;

INSERT INTO "orders"."order_book" ("order_id", "book_id", "price", "quantity")
VALUES
(1, 1, 530.90, 5),
(1, 2, 1000, 1),
(1, 3, 53.90, 1),
(1, 4, 30.90, 1),
(2, 1, 0.90, 1),
(2, 5, 353.90, 1),
(2, 7, 230.90, 1),
(2, 8, 10.90, 1),
(3, 20, 999.99, 3),
(6, 45, 79.5, 2),
(7, 33, 999.7, 1),
(4, 3, 999.90, 1),
(4, 99, 1000, 2),
(4, 200, 53.90, 5),
(5, 4, 30.90, 6),
(5, 1, 20.90, 1),
(5, 5, 353.90, 2),
(5, 7, 230.90, 1),
(4, 8, 10.90, 1),
(5, 20, 999.99, 4),
(4, 45, 79.5, 2),
(5, 33, 999.7, 1)
;
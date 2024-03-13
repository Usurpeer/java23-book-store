INSERT INTO "orders"."customer" ("first_name", "last_name", "middle_name", "login")
VALUES
('Имя1', 'Фамилия1', NULL, 'test@example.com'),
('Имя2', 'Фамилия2', 'Отчество', 'test@example.com'),
('Имя3', 'Фамилия3', 'Отчество3', '@example.com')
;

INSERT INTO "orders"."order" ("customer_id", "status")
VALUES
(1, 'PROCESSING'),
(1, 'CLOSED'),
(1, 'PROCESSING'),
(2, 'PROCESSING'),
(2, 'CLOSED')
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
(2, 8, 10.90, 1)
;
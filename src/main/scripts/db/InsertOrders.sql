INSERT INTO "orders"."customer" ("first_name", "last_name", "middle_name", "email")
VALUES
('Имя1', 'Фамилия1', NULL, 'test@example.com')
;

INSERT INTO "orders"."order" ("customer_id", "status")
VALUES
(1, 'PROCESSING')
;

INSERT INTO "orders"."order_book" ("order_id", "book_id", "price", "quantity")
VALUES
(1, 1, 530.90, 5)
;
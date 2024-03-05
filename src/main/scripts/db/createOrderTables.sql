create schema "orders";

CREATE TABLE "orders"."customer" (
    "id" bigserial NOT NULL,
    "first_name" varchar(64) not null,
    "last_name" varchar(64) not null,
    "middle_name" varchar(64),
    "email" varchar(255),

    PRIMARY KEY (id)
);


CREATE TABLE "orders"."order" (
    "id" bigserial NOT NULL,
    "created" timestamp default now(),
    "customer_id" bigint not null,
    "status" varchar(15) not null default 'PROCESSING',

    PRIMARY KEY (id),

	FOREIGN KEY (customer_id)
		REFERENCES "orders"."customer"(id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);
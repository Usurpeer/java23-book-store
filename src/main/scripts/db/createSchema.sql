create schema if not exists "catalog";
ALTER SCHEMA "catalog" OWNER TO books_store_user;
create schema if not exists "orders";
ALTER SCHEMA "orders" OWNER TO books_store_user;


drop table if exists "orders"."order_book";
drop table if exists "orders"."order";
drop table if exists "orders"."customer";

drop table if exists "catalog"."book_authors";
drop table if exists "catalog"."book_genres";
drop table if exists "catalog"."book";
drop table if exists "catalog"."author";
drop table if exists "catalog"."genre";


create table "catalog"."genre" (
	"id" bigserial primary key,
	"title" text
);
ALTER TABLE "catalog"."genre" OWNER TO books_store_user;

create table "catalog"."author" (
	"id" bigserial primary key,
    "first_name" varchar(64) not null,
    "last_name" varchar(64) not null,
    "middle_name" varchar(64),
    "pseudonym" varchar(255)
);
ALTER TABLE "catalog"."author" OWNER TO books_store_user;

create table "catalog"."book" (
    "id" bigserial primary key,
    "title" text,
    "description" text,
    "status" text not null default 'CLOSED',
    "price" decimal(10,2) not null,
    "publisher" text,
    "image_name" text DEFAULT 'default_book.png' not null,
    "year_publication" int
);
ALTER TABLE "catalog"."book" OWNER TO books_store_user;


	--таблицы для связи книг и авторов
CREATE TABLE "catalog"."book_authors" (
    book_id BIGINT REFERENCES catalog.book(id) ON DELETE CASCADE ON UPDATE CASCADE,
    author_id BIGINT REFERENCES catalog.author(id),
    PRIMARY KEY (book_id, author_id)
);
ALTER TABLE "catalog"."book_authors" OWNER TO books_store_user;

CREATE TABLE "catalog"."book_genres" (
    book_id BIGINT REFERENCES catalog.book(id) ON DELETE CASCADE ON UPDATE CASCADE,
    genre_id BIGINT REFERENCES catalog.genre(id),
    PRIMARY KEY (book_id, genre_id)
);
ALTER TABLE "catalog"."book_genres" OWNER TO books_store_user;

--следующая схема
CREATE TABLE "orders"."customer" (
    "id" bigserial NOT NULL,
    "first_name" varchar(64) not null,
    "last_name" varchar(64) not null,
    "middle_name" varchar(64),
    "login" varchar(255)  not null,

    PRIMARY KEY (id),
    UNIQUE ("login")
);
ALTER TABLE "orders"."customer" OWNER TO books_store_user;


CREATE TABLE "orders"."order" (
    "id" bigserial NOT NULL,
    "created" timestamp default now(),
    "customer_id" bigint not null,
    "status" text not null default 'PROCESSING',

    PRIMARY KEY (id),

	FOREIGN KEY (customer_id)
		REFERENCES "orders"."customer"(id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);
ALTER TABLE "orders"."order" OWNER TO books_store_user;

-- после двух схем
create table "orders"."order_book" (
	"order_id" bigint,
	"book_id" bigint,
	"price" decimal(10,2) not null,
	"quantity" int not null default 1,

	foreign key ("order_id")
		references "orders"."order"("id")
		on delete cascade,
	foreign key ("book_id")
		references "catalog"."book"("id")
		on delete restrict,
	primary key ("order_id", "book_id")
);
ALTER TABLE "orders"."order_book" OWNER TO books_store_user;

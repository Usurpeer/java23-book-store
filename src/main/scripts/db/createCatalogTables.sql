create schema "catalog";

create table "catalog"."genre" (
	"id" bigserial primary key,
	"title" text
);

create table "catalog"."author" (
	"id" bigserial primary key,
    "first_name" varchar(64) not null,
    "last_name" varchar(64) not null,
    "middle_name" varchar(64),
    "pseudonym" varchar(255)
);

create table "catalog"."book" (
    "id" bigserial primary key,
    "title" text,
    "description" text,
    "status" varchar(15) not null default 'CLOSED',
    "price" decimal(10,2) not null,
    "publisher" text,
    "image_name" text DEFAULT 'default_book.png' not null,
    "year_publication" int
);


	--таблицы для связи книг и авторов
CREATE TABLE catalog.book_authors (
    book_id BIGINT REFERENCES catalog.book(id),
    author_id BIGINT REFERENCES catalog.author(id),
    PRIMARY KEY (book_id, author_id)
);

CREATE TABLE catalog.book_genres (
    book_id BIGINT REFERENCES catalog.book(id),
    genre_id BIGINT REFERENCES catalog.genre(id),
    PRIMARY KEY (book_id, genre_id)
);
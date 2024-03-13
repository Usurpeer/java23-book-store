DELETE FROM "catalog"."book";
ALTER SEQUENCE "catalog"."book_id_seq" RESTART WITH 1;

truncate table "catalog"."author" restart identity cascade;
truncate table "catalog"."genre" restart identity cascade;
truncate table "catalog"."book_authors" restart identity cascade;
truncate table "catalog"."book_genres" restart identity cascade;
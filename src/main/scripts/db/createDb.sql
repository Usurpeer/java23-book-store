-- RUN WITH postgres USER
CREATE USER books_store_user WITH PASSWORD 'admin';
CREATE DATABASE books_store OWNER books_store_user;
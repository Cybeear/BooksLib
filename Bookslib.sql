CREATE DATABASE "BooksLib"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

create table "Book"
(
    id     serial
        primary key,
    name   varchar(20) not null,
    author varchar(20) not null
);

alter table "Book"
    owner to postgres;

create table "Reader"
(
    id   serial
        primary key,
    name varchar(20) not null
);

alter table "Reader"
    owner to postgres;

create table "Borrow"
(
    id        serial
        primary key,
    reader_id integer not null
        constraint reader_id
            references "Reader"
            on update cascade on delete cascade,
    book_id   integer not null
        constraint book_id
            references "Book"
);

alter table "Borrow"
    owner to postgres;


DROP TABLE IF EXISTS "Book";
DROP TABLE IF EXISTS "Reader";
DROP TABLE IF EXISTS "Borrow";

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


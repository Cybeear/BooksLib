DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS Reader;
DROP TABLE IF EXISTS Borrow;

create table book
(
    id     serial
        primary key,
    name   varchar(20) not null,
    author varchar(20) not null
);

alter table book
    owner to postgres;

create table reader
(
    id   serial
        primary key,
    name varchar(20) not null
);

alter table reader
    owner to postgres;

create table borrow
(
    id        serial
        primary key,
    reader_id integer not null
        constraint reader_id
            references Reader
            on update cascade on delete cascade,
    book_id   integer not null
        constraint book_id
            references book
);

alter table borrow
    owner to postgres;


DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS reader;
DROP TABLE IF EXISTS borrow;

CREATE TABLE book
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(20) NOT NULL,
    author VARCHAR(20) NOT NULL
);

CREATE TABLE reader
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE borrow
(
    id        SERIAL PRIMARY KEY,
    reader_id INTEGER NOT NULL,
    book_id   INTEGER NOT NULL,
    CONSTRAINT FK_READER FOREIGN KEY (reader_id) REFERENCES reader (id) ON DELETE CASCADE,
    CONSTRAINT FK_BOOK FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE,
    CONSTRAINT Borrow_Unique UNIQUE (reader_id, book_id)
        INCLUDE(reader_id, book_id)
);
INSERT INTO book(name, author)
VALUES ('test', 'test'),
       ('test1', 'test1'),
       ('test2', 'test2'),
       ('test3', 'test3'),
       ('test4', 'test4'),
       ('test5', 'test5'),
       ('test6', 'test6');


INSERT INTO reader(name)
VALUES ('test'),
       ('test1'),
       ('test2'),
       ('test3'),
       ('test4'),
       ('test5'),
       ('test6');


INSERT INTO borrow(reader_id, book_id)
VALUES (1, 1),
       (1, 2),
       (2, 4),
       (3, 5);
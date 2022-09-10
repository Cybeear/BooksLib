package dao;

import entity.Book;

import java.util.List;

public interface BookDao {
    Book save(Book book);

    List<Book> findAll();

    Book findById(long bookId);

    List<Book> findAllByReaderId(long readerId);
}

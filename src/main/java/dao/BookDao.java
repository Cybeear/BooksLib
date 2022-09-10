package dao;

import entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> save(Book book);

    List<Book> findAll();

    Optional<Book> findById(long bookId);

    List<Book> findAllByReaderId(long readerId);
}

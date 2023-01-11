package repositories;

import entities.Book;

import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Book findById(long bookId);

    List<Book> findAllByReaderId(long readerId);
}

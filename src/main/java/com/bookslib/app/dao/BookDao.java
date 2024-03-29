package com.bookslib.app.dao;

import com.bookslib.app.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(long bookId);

    List<Book> findAllByReaderId(long readerId);
}

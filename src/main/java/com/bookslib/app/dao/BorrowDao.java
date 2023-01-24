package com.bookslib.app.dao;

import com.bookslib.app.entity.Borrow;

import java.util.List;
import java.util.Optional;

public interface BorrowDao {
    Optional<Borrow> save(long bookId, long readerId);

    List<Borrow> findAll();

    int returnBook(long bookId, long readerId);
}
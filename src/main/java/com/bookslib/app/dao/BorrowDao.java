package com.bookslib.app.dao;

import com.bookslib.app.entity.Borrow;

public interface BorrowDao {
    void save(Borrow borrow);

    void returnBook(Borrow borrow);
}
package dao;

import entity.Book;
import entity.Borrow;

import java.util.List;
import java.util.Optional;

public interface BorrowDao {
    Optional<Borrow> save(long bookId, long readerId);

    List<Borrow> findAll();

    void returnBook(long bookId, long readerId);
}
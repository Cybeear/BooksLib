package dao;

import entity.Book;
import entity.Borrow;

import java.util.List;
import java.util.Optional;

public interface BorrowDao {
    Borrow save(long bookId, long readerId);

    List<Borrow> findAll();

    List<Borrow> findAllBorrowedByReaderId(long readerId);

    List<Borrow> findAllBorrowedByBookId(long bookId);

    List<Borrow> findAllReadersWithTheirBorrows();

    List<Borrow> findAllBooksWithTheirBorrowers();

    void returnBook(long bookId, long readerId);
}
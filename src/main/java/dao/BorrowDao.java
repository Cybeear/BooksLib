package dao;

import entity.Borrow;

import java.util.List;
import java.util.Optional;

public interface BorrowDao {
    Optional<Borrow> save(long bookId, long readerId);

    List<Borrow> findAll();

    List<Borrow> findAllReadersWithTheirBorrows();

    List<Borrow> findAllBooksWithTheirBorrowers();

    int returnBook(long bookId, long readerId);
}
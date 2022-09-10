package dao;

import entity.Borrow;

import java.util.List;

public interface BorrowDao {
    Borrow save(long bookId, long readerId);

    List<Borrow> findAll();

    void returnBook(long bookId, long readerId);
}
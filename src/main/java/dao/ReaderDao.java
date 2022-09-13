package dao;

import entity.Book;
import entity.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderDao {
    Reader save(Reader reader);

    List<Reader> findAll();

    Optional<Reader> findById(long readerId);

    List<Reader> findAllByBookId(long bookId);
}

package dao;

import entity.Reader;

import java.util.List;

public interface ReaderDao {
    Reader save(Reader reader);

    List<Reader> findAll();

    Reader findById(long readerId);

    List<Reader> findAllByBookId(long bookId);
}

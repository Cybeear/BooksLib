package repositories;

import entities.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderRepository {
    Reader save(Reader reader);

    List<Reader> findAll();

    Optional<Reader> findById(long readerId);

    List<Reader> findAllByBookId(long bookId);
}

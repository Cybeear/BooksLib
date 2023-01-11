package services;

import entities.Reader;
import exceptions.LibraryServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ReaderRepository;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class ReaderService {
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    ParserService parserService;

    /**
     * Show all readers in the list
     */
    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    /**
     * Add new reader to list
     */
    public Reader registerReader(String newReaderName) {
        if (newReaderName.isBlank()) {
            throw new LibraryServiceException("Reader name can not be an empty string!");
        }
        return readerRepository.save(
                new Reader(newReaderName));
    }

    /**
     * @param bookId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public List<Reader> getWhoBorrowByBookId(String bookId) {
        var parsed = parserService.parseLong(bookId.trim());
        return readerRepository.findAllByBookId(parsed);
    }
}

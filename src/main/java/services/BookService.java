package services;

import entities.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ParserService parserService;

    /**
     * Show all books in the list
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Add new book to list
     */
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * @param bookId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print all borrow objects by reader id
     */
    public Optional<Book> getById(long bookId) {
        return bookRepository.findById(bookId);
    }

    /**
     * @param readerId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print all borrow objects by reader id
     */
    public List<Book> getAllBorrowedByReaderId(Long readerId) {
        return bookRepository.findAllByReaderId(readerId);
    }
}

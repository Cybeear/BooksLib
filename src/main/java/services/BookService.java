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
     * @param readerId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print all borrow objects by reader id
     */
    public List<Book> getAllBorrowedByReaderId(String readerId) {
        var parsed = parserService.parseLong(readerId.trim());
        return bookRepository.findAllByReaderId(parsed);
    }
}

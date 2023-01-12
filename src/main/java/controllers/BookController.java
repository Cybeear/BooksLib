package controllers;

import entities.Book;
import exceptions.BookServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library/book")
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public ResponseEntity<List<Book>> getAllBooks(HttpServletRequest request) {
        log.info("Trying to get all books user ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        List<Book> books = bookService.getAllBooks();
        if (!books.isEmpty()) {
            log.info("Books is exists!");
            return new ResponseEntity<>(books, HttpStatus.OK);
        } else {
            log.info("Books doesn`t exists!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id={bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId,
                                            HttpServletRequest request) {
        log.info("Trying to get book by id \tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        var book = bookService.getById(bookId);
        if (book.isPresent()) {
            log.info("Book is exists!");
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            log.info("Book doesn`t exists!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getAllBy?id={readerId}")
    public ResponseEntity<List<Book>> getAllBorrowedBooksByReaderId(@PathVariable Long readerId,
                                                                    HttpServletRequest request) {
        log.info("Trying to get all books by reader id \tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        List<Book> books = bookService.getAllBorrowedByReaderId(readerId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<Book> saveNewBook(@RequestBody Book book,
                                            HttpServletRequest request) {
        log.info("Trying to create new book: name - "
                + book.getName() + ", author - "
                + book.getAuthor() + "\tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        Book createdBook = new Book(book.getName(), book.getAuthor());
        try {
            bookService.addBook(createdBook);
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        } catch (BookServiceException bookServiceException) {
            log.error(bookServiceException.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

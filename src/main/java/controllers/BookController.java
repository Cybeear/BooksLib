package controllers;

import entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (!books.isEmpty()) {
            return new ResponseEntity<>(books, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/id={bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        var book = bookService.getById(bookId);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/getAllBy?id={readerId}")
    public ResponseEntity<List<Book>> getAllBorrowedBooksByReaderId(@PathVariable Long readerId) {
        List<Book> books = bookService.getAllBorrowedByReaderId(readerId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<Book> saveNewBook(@RequestBody Book book) {
        Book createdBook = new Book(book.getName(), book.getAuthor());
        bookService.addBook(createdBook);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }
}

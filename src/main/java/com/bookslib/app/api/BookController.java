package com.bookslib.app.api;

import com.bookslib.app.entity.Book;
import com.bookslib.app.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/library/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping
    public ResponseEntity<Book> saveNewBook(@RequestBody @Valid Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getByBookId(@PathVariable(name = "bookId")
                                            @Min(value = 1,
                                                    message = "Book ID must be a positive number and higher then 0")
                                            long bookId) {
        return bookService
                .getBookById(bookId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/readers/{readerId}")
    public ResponseEntity<List<Book>> getListByReaderId(@PathVariable(name = "readerId")
                                                        @Min(value = 1,
                                                                message = "Reader ID must be a positive number and higher then 0")
                                                        long readerId) {
        var books = bookService.getAllBorrowedByReaderId(readerId);
        return !books.isEmpty() ? ResponseEntity.ok(books) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}

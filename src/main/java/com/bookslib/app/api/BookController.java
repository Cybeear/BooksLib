package com.bookslib.app.api;

import com.bookslib.app.entity.Book;
import com.bookslib.app.entity.Reader;
import com.bookslib.app.service.BookService;
import com.bookslib.app.service.ReaderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ReaderService readerService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping
    public ResponseEntity<Book> saveNewBook(@RequestBody @Valid Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getByBookId(
            @PathVariable(name = "bookId")
            @Min(value = 1,
                    message = "Book ID must be a positive number and higher then 0")
            long bookId) {
        return bookService
                .getBookById(bookId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{bookId}/readers")
    public ResponseEntity<List<Reader>> getListByReaderId(
            @PathVariable(name = "bookId")
            @Min(value = 1,
                    message = "Reader ID must be a positive number and higher then 0")
            long bookId) {
        var readers = readerService.getWhoBorrowByBookId(bookId);
        return readers.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(readers);
    }


}

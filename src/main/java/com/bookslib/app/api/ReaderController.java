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
@RequestMapping("/api/v1/library/readers")
@RequiredArgsConstructor
@Slf4j
public class ReaderController {
    private final ReaderService readerService;
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        return ResponseEntity.ok(readerService.getAllReaders());
    }

    @PostMapping
    public ResponseEntity<Reader> addNewReader(@RequestBody @Valid Reader reader) {
        return ResponseEntity.ok(readerService.registerReader(reader));
    }

    @GetMapping("/{readerId}")
    public ResponseEntity<Reader> getByReaderId(
            @PathVariable(name = "readerId")
            @Min(value = 1,
                    message = "Reader ID must be a positive number and higher then 0")
            long readerId) {
        return readerService
                .getReaderByReaderId(readerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{readerId}/books")
    public ResponseEntity<List<Book>> getReaderByBookId(
            @PathVariable(name = "readerId")
            @Min(value = 1,
                    message = "reader ID must be a positive number and higher then 0")
            long readerId) {
        var books = bookService.getAllBorrowedByReaderId(readerId);
        return books.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(books);
    }

}

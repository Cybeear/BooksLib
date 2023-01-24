package com.bookslib.app.api;

import com.bookslib.app.entity.Reader;
import com.bookslib.app.service.ReaderService;
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
@RequestMapping("/api/v1/library/readers")
@RequiredArgsConstructor
@Slf4j
public class ReaderController {
    private final ReaderService readerService;

    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        return ResponseEntity.ok(readerService.getAllReaders());
    }

    @PostMapping
    public ResponseEntity<Reader> addNewReader(@RequestBody @Valid Reader reader) {
        return ResponseEntity.ok(readerService.registerReader(reader));
    }

    @GetMapping("/{readerId}")
    public ResponseEntity<Reader> getByReaderId(@PathVariable(name = "readerId")
                                                @Min(value = 1,
                                                        message = "Reader ID must be a positive number and higher then 0")
                                                long readerId) {
        return readerService
                .getReaderByReaderId(readerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{bookId}/books")
    public ResponseEntity<List<Reader>> getReaderByBookId(@PathVariable(name = "bookId")
                                                          @Min(value = 1,
                                                                  message = "Book ID must be a positive number and higher then 0")
                                                          long bookId) {
        var readers = readerService.getWhoBorrowByBookId(bookId);
        return readers.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(readers);
    }

}

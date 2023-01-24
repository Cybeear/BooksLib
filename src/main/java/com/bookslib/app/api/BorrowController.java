package com.bookslib.app.api;

import com.bookslib.app.entity.Borrow;
import com.bookslib.app.service.BorrowService;
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
@RequestMapping("/api/v1/library/borrows")
@RequiredArgsConstructor
@Slf4j
public class BorrowController {
    private final BorrowService borrowService;

    @GetMapping
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        return ResponseEntity.ok(borrowService.getAll());
    }

    @PostMapping
    public ResponseEntity<Borrow> addNewBorrow(
            @Min(value = 1, message = "Reader ID must be a positive number and higher then 0") long readerId,
            @Min(value = 1, message = "Book ID must be a positive number and higher then 0") long bookId) {
        return borrowService
                .borrowBook(readerId, bookId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteBorrow(
            @Min(value = 1, message = "Reader ID must be a positive number and higher then 0") long readerId,
            @Min(value = 1, message = "Book ID must be a positive number and higher then 0") long bookId) {
        return borrowService.returnBook(readerId, bookId) == 1 ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

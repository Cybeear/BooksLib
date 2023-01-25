package com.bookslib.app.api;

import com.bookslib.app.entity.Borrow;
import com.bookslib.app.service.BorrowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/library/borrows")
@RequiredArgsConstructor
@Slf4j
public class BorrowController {
    private final BorrowService borrowService;

    @PostMapping
    public ResponseEntity<Borrow> addNewBorrow(@RequestBody Borrow borrow) {
        borrowService.borrowBook(borrow);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteBorrow(@RequestBody Borrow borrow) {
        borrowService.returnBook(borrow);
        return ResponseEntity.ok().build();

    }
}

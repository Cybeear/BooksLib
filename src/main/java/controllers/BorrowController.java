package controllers;

import entities.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.BorrowService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library/borrow")
public class BorrowController {

    @Autowired
    BorrowService borrowService;

    @GetMapping("/")
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        return new ResponseEntity<>(borrowService.getAllBorrows(), HttpStatus.OK);
    }

    @GetMapping("/byBooks")
    public ResponseEntity<List<Borrow>> getAllBorrowsByBooks() {
        var borrows = borrowService.getAllReadersWithTheirBorrows();
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    @GetMapping("/byReaders")
    public ResponseEntity<List<Borrow>> getAllBorrowsByReaders() {
        var borrows = borrowService.getAllBooksWithTheirBorrowers();
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Borrow> saveNewBorrow(@RequestParam(name = "reader_id") Long readerId,
                                                @RequestParam(name = "book_id") Long bookId) {
        var createdBorrow = borrowService.borrowBook(bookId, readerId);
        if (createdBorrow.isPresent()) {
            return new ResponseEntity<>(createdBorrow.get(), HttpStatus.CREATED);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}

package controllers;

import entities.Borrow;
import exceptions.BorrowRepositoryException;
import exceptions.BorrowServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import services.BorrowService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library/borrow")
@Slf4j
public class BorrowController {

    @Autowired
    BorrowService borrowService;

    @GetMapping("/")
    public ResponseEntity<List<Borrow>> getAllBorrows(HttpServletRequest request) {
        log.info("Trying to get all borrows "
                + "\tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        return new ResponseEntity<>(borrowService.getAllBorrows(), HttpStatus.OK);
    }

    @GetMapping("/byBooks")
    public ResponseEntity<List<Borrow>> getAllBorrowsByBooks(HttpServletRequest request) {
        log.info("Trying to get all borrows by books"
                + "\tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        var borrows = borrowService.getAllReadersWithTheirBorrows();
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    @GetMapping("/byReaders")
    public ResponseEntity<List<Borrow>> getAllBorrowsByReaders(HttpServletRequest request) {
        log.info("Trying to get all borrows by readers"
                + "\tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        var borrows = borrowService.getAllBooksWithTheirBorrowers();
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<Borrow> saveNewBorrow(@RequestParam(name = "reader_id") Long readerId,
                                                @RequestParam(name = "book_id") Long bookId,
                                                HttpServletRequest request) {
        log.info("Trying to save new Borrow: reader_id - "
                + readerId + " book - " + bookId
                + "\tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        try {
            var createdBorrow = borrowService.borrowBook(bookId, readerId);
            log.info("Borrow is created!");
            return new ResponseEntity<>(createdBorrow.get(), HttpStatus.CREATED);
        } catch (BorrowServiceException borrowServiceException) {
            log.error("Borrow doesn`t created! Maybe this reader was borrow this book! "
                    + borrowServiceException.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/return")
    @Transactional
    public ResponseEntity<String> deleteBorrow(@RequestParam(name = "reader_id") Long readerId,
                                               @RequestParam(name = "book_id") Long bookId,
                                               HttpServletRequest request) {
        log.info("Trying to delete Borrow: reader_id - "
                + readerId + " book - " + bookId
                + "\tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        try {
            borrowService.returnBook(readerId, bookId);
            log.info("Borrow was deleted!");
            return new ResponseEntity<>("Borrow was deleted!",
                    HttpStatus.ACCEPTED);
        } catch (BorrowRepositoryException borrowRepositoryException) {
            log.error(borrowRepositoryException.getLocalizedMessage());
            return new ResponseEntity<>("Borrow wasn`t deleted! Maybe this reader didn`t borrow this book!",
                    HttpStatus.BAD_REQUEST);
        }
    }

}

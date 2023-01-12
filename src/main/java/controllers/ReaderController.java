package controllers;

import entities.Reader;
import exceptions.ReaderServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import services.ReaderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library/reader")
@Slf4j
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping("/")
    public ResponseEntity<List<Reader>> getAllReaders(HttpServletRequest request) {
        log.info("Trying to get all readers user ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        List<Reader> readers = readerService.getAllReaders();
        if (!readers.isEmpty()) {
            log.info("Readers is exists!");
            return new ResponseEntity<>(readers, HttpStatus.OK);
        } else {
            log.info("Readers doesn`t exists!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id={readerId}")
    public ResponseEntity<Reader> getReaderById(@PathVariable Long readerId,
                                                HttpServletRequest request) {
        log.info("Trying to get reader by id \tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        var readers = readerService.findById(readerId);
        if (readers.isPresent()) {
            log.info("Reader is exists!");
            return new ResponseEntity<>(readers.get(), HttpStatus.OK);
        } else {
            log.info("Reader doesn`t exists!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllBy?id={bookId}")
    public ResponseEntity<List<Reader>> findAllReadersByBookId(@PathVariable Long bookId,
                                                               HttpServletRequest request) {
        log.info("Trying to get all readers by book id \tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        List<Reader> readers = readerService.getWhoBorrowByBookId(bookId);
        return new ResponseEntity<>(readers, HttpStatus.OK);
    }


    @PostMapping("/")
    @Transactional
    public ResponseEntity<Reader> saveNewReader(@RequestBody Reader reader,
                                                HttpServletRequest request) {
        log.info("Trying to create new reader: name - "
                + reader.getName()
                + "\tuser ip addr: "
                + request.getRemoteAddr()
                + "\tuser header"
                + request.getHeader("User-Agent"));
        try {
            Reader createdReader = readerService.registerReader(reader.getName());
            return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
        } catch (ReaderServiceException readerServiceException) {
            log.error(readerServiceException.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}

package controllers;

import entities.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ReaderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library/reader")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping("/")
    public ResponseEntity<List<Reader>> getAllReaders() {
        List<Reader> readers = readerService.getAllReaders();
        if (!readers.isEmpty()) {
            return new ResponseEntity<>(readers, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/id={readerId}")
    public ResponseEntity<Reader> getReaderById(@PathVariable Long readerId) {
        var readers = readerService.findById(readerId);
        if (readers.isPresent()) {
            return new ResponseEntity<>(readers.get(), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getAllBy?id={bookId}")
    public ResponseEntity<List<Reader>> findAllReadersByBookId(@PathVariable Long bookId) {
        List<Reader> readers = readerService.getWhoBorrowByBookId(bookId);
        return new ResponseEntity<>(readers, HttpStatus.OK);
    }


    @PostMapping("/")
    public ResponseEntity<Reader> saveNewReader(@RequestBody Reader reader) {
        Reader createdReader = readerService.registerReader(reader.getName());
        return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
    }


}

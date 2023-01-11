package controllers;

import entities.Book;
import entities.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library/book")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = bookService.getAllBooks();
        if (!books.isEmpty()){
        return new ResponseEntity<>(books, HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<Book> save(@RequestBody Book book){
        Book createdBook = new Book(book.getName(),book.getAuthor());
        try{
            bookService.addBook(createdBook);
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}

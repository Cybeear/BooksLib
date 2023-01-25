package com.bookslib.app.service;

import com.bookslib.app.dao.BookDao;
import com.bookslib.app.entity.Book;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Data
@Slf4j
public class BookService {
    private final BookDao bookDao;

    /**
     * Show all books in the list
     */
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    /**
     * @param bookId
     * @return
     */
    public Optional<Book> getBookById(long bookId) {
        return bookDao.findById(bookId);
    }

    /**
     * Add new book to list
     */
    public Book addBook(Book book) {
        return bookDao.save(book);
    }

    /**
     * @param readerId
     * @return Return to menu if string of arguments contains any symbols other than numbers or
     * print all borrow objects by reader id
     */
    public List<Book> getAllBorrowedByReaderId(long readerId) {
        return bookDao.findAllByReaderId(readerId);
    }
}

package com.bookslib.app.service;

import com.bookslib.app.exceptions.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
@Data
@Slf4j
public class UIService {
    private final LibraryService libraryService;
    private final Scanner in = new Scanner(System.in);

    public void drawMenu() {
        var str = """
                -----------------------------------------------------------------------------------------------------
                | PLEASE, SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION'S NUMBER AND PRESSING ENTER KEY:  |
                |                              [1] SHOW ALL BOOKS IN THE LIBRARY                                     |
                |                              [2] SHOW ALL READERS REGISTERED IN THE LIBRARY                        |
                |                              [3]REGISTER NEW READER                                                |
                |                              [4]ADD NEW BOOK                                                       |
                |                              [5]BORROW A BOOK TO A READER                                          |
                |                              [6]RETURN A BOOK TO THE LIBRARY                                       |
                |                              [7]SHOW ALL BORROWED BOOK BY USER ID                                  |
                |                              [8]SHOW CURRENT READER OF A BOOK WITH ID                              |
                |                              [9]SHOW ALL READERS WITH THEIR BORROWED BOOKS                         |
                |                              [10]SHOW ALL BOOKS WITH THEIR BORROWERS                               |
                |                             "TYPE 'exit' TO STOP THE PROGRAM AND EXIT!"                            |
                -----------------------------------------------------------------------------------------------------""";
        log.info(str);
    }

    public void showMenu() {
        drawMenu();
        String option = in.nextLine();
        switch (option) {
            case "1" -> showAllBooks();
            case "2" -> showAllReaders();
            case "3" -> registerReader();
            case "4" -> addBook();
            case "5" -> borrowBook();
            case "6" -> returnBook();
            case "7" -> showAllBorrowedByReaderId();
            case "8" -> showWhoBorrowByBookId();
            case "9" -> showAllReadersWithTheirBorrows();
            case "10" -> showAllBooksWithTheirBorrowers();
            case "exit" -> System.exit(0);
            default -> System.out.println("Program dont have this option!");
        }
    }

    private void showAllBooks() {
        try {
            var books = libraryService.getAllBooks();
            if (!books.isEmpty()) {
                books.forEach(System.out::println);
            } else {
                log.info("Database have not any books!");
            }
        } catch (BookDaoException e) {
            log.info("Database error: " + e);
        }
    }

    private void showAllReaders() {
        try {
            var readers = libraryService.getAllReaders();
            if (!readers.isEmpty()) {
                readers.forEach(System.out::println);
            } else {
                log.info("Database have not any readers!");
            }
        } catch (ReaderDaoException e) {
            log.info("Database error: " + e);
        }
    }

    private void registerReader() {
        System.out.println("Please enter new reader full name!");
        try {
            var newReaderName = in.nextLine();
            var reader = libraryService.registerReader(newReaderName);
            log.info(reader + " successful registered!");
        } catch (LibraryServiceException e) {
            log.info("Failed to create new reader: " + e.getMessage());
        } catch (ReaderDaoException e) {
            log.info("Failed to save reader: " + e.getMessage());
        }
    }

    private void addBook() {
        System.out.println("Please enter new book name and author separated by '/'. Like this: name / author!");
        try {
            var newBookData = in.nextLine();
            var book = libraryService.addBook(newBookData);
            log.info(book + " successful added!");
        } catch (LibraryServiceException e) {
            log.info("Failed to create new book: " + e.getMessage());
        } catch (BookDaoException e) {
            log.info("Failed to save book: " + e.getMessage());
        }
    }

    private void borrowBook() {
        System.out.println("Please enter reader id and book id to borrow separated by '/'. Like this: 4 / 2!");
        try {
            var readeAndBookIds = in.nextLine();
            libraryService.borrowBook(readeAndBookIds).ifPresentOrElse(System.out::println,
                    () -> log.info("Error: book or reader is not exists!"));
        } catch (ParserServiceException e) {
            log.info("Failed to parse reader id or book id: " + e.getMessage());
        } catch (LibraryServiceException e) {
            log.info("Failed to create borrow: " + e.getMessage());
        } catch (BorrowDaoException e) {
            log.info("Failed to borrow a book: " + e.getMessage());
        }
    }

    private void returnBook() {
        System.out.println("Please enter reader id and book id to return separated by '/'. Like this: 1 / 3!");
        try {
            var readeAndBookIds = in.nextLine();
            libraryService.returnBook(readeAndBookIds);
            log.info("Book is returned!");
        } catch (ParserServiceException e) {
            log.info("Failed to parse reader id or book id: " + e.getMessage());
        } catch (LibraryServiceException e) {
            log.info("Failed to create borrow: " + e.getMessage());
        } catch (BorrowDaoException e) {
            log.info("Failed to return book: " + e.getMessage());
        }
    }

    private void showAllBorrowedByReaderId() {
        System.out.println("Please enter reader id: ");
        try {
            var readerId = in.nextLine();
            var books = libraryService.getAllBorrowedByReaderId(readerId);
            if (!books.isEmpty()) {
                books.forEach(System.out::println);
            } else {
                log.info("This reader isn`t borrow books!");
            }
        } catch (ParserServiceException e) {
            log.info("Failed to parse reader id: " + e.getMessage());
        } catch (LibraryServiceException e) {
            log.info("Failed to get books: " + e.getMessage());
        } catch (BookDaoException e) {
            log.info("Failed to fetch books: " + e.getMessage());
        }
    }

    private void showWhoBorrowByBookId() {
        System.out.println("Please enter book id: ");
        try {
            var bookId = in.nextLine();
            var readers = libraryService.getWhoBorrowByBookId(bookId);
            if (!readers.isEmpty()) {
                readers.forEach(System.out::println);
            } else {
                log.info("This book isn`t borrowed!");
            }
        } catch (ParserServiceException e) {
            log.info("Failed to parse reader id: " + e.getMessage());
        } catch (LibraryServiceException e) {
            log.info("Failed to get readers: " + e.getMessage());
        } catch (ReaderDaoException e) {
            log.info("Failed to fetch readers: " + e.getMessage());
        }
    }

    private void showAllReadersWithTheirBorrows() {
        try {
            var borrows = libraryService.getAllReadersWithTheirBorrows();
            if (!borrows.isEmpty()) {
                borrows.forEach(System.out::println);
            } else {
                log.info("No active readers at the moment!");
            }
        } catch (BorrowDaoException e) {
            log.info("Database error: " + e.getMessage());
        }
    }

    private void showAllBooksWithTheirBorrowers() {
        var borrows = libraryService.getAllBooksWithTheirBorrowers();
        try {
            if (!borrows.isEmpty()) {
                borrows.forEach(System.out::println);
            } else {
                log.info("At the moment no books are borrowed!");
            }
        } catch (BorrowDaoException e) {
            log.info("Database error: " + e.getMessage());
        }
    }
}
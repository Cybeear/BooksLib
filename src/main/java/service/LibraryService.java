package service;

import dao.BookDaoJdbcImpl;
import dao.BorrowBorrowDaoJdbcImpl;
import dao.ReaderDaoJdbcImpl;
import entity.Book;
import entity.Borrow;
import entity.Reader;


/**
 * LibraryService class used to interaction with data and objects in online Library
 */
public class LibraryService {
    BookDaoJdbcImpl bookDaoJdbcImpl;
    ReaderDaoJdbcImpl readerDaoJdbcImpl;
    BorrowBorrowDaoJdbcImpl borrowDaoJdbcImpl;
    ParserService parserService;

    {
        bookDaoJdbcImpl = new BookDaoJdbcImpl();
        readerDaoJdbcImpl = new ReaderDaoJdbcImpl();
        borrowDaoJdbcImpl = new BorrowBorrowDaoJdbcImpl();
        parserService = new ParserService();
    }

    /**
     * Add new reader to list
     */
    public void registerReader(String str) {
        if (!str.equals(" ")) readerDaoJdbcImpl.save(new Reader(str));
        else System.err.println("Error: enter a valid data!");
    }

    /**
     * Add new book to list
     */
    public void addBook(String str) {
        var inputSplit = str.split(" / ");
        if (inputSplit.length < 2
                || inputSplit[0].equals(" ")
                || inputSplit[1].equals(" ")) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        bookDaoJdbcImpl.save(new Book(inputSplit[0], inputSplit[1]));
    }

    /**
     * Show all books in the list
     */
    public void showAllBooks() {
        bookDaoJdbcImpl.findAll().forEach(System.out::println);
    }

    /**
     * Show all readers in the list
     */
    public void showAllReaders() {
        readerDaoJdbcImpl.findAll().forEach(System.out::println);
    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * add to borrowList if string not contains any characters other than numbers
     */
    public void borrowABook(String str) {
        var inputSplit = str.split(" / ");
        if (inputSplit.length < 2) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var bookToBorrow = bookDaoJdbcImpl.findById(bookId);
        var readerToBorrow = readerDaoJdbcImpl.findById(readerId);
        if (bookToBorrow.isPresent() && readerToBorrow.isPresent()) {
            var borrow = new Borrow(bookToBorrow.get(), readerToBorrow.get());
            borrowDaoJdbcImpl.save(bookId, readerId);
            System.out.println(borrow);
        } else System.err.println("Error: data is not exists!");
    }

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * delete object from borrowList if string not contains any characters other than numbers
     */
    public void returnBorrowedBook(String str) {
        var inputSplit = str.split(" / ");
        if (inputSplit.length < 2) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var readerId = parserService.parseLong(inputSplit[0]);
        var bookId = parserService.parseLong(inputSplit[1]);
        if (readerId == -1 || bookId == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var bookToBorrow = bookDaoJdbcImpl.findById(bookId);
        var readerToBorrow = readerDaoJdbcImpl.findById(readerId);
        if (bookToBorrow.isPresent() && readerToBorrow.isPresent()) {
            borrowDaoJdbcImpl.returnBook(bookId, readerId);
        } else System.err.println("Error: data is not exists");
    }


    /**
     * Return to menu if string of arguments contains any symbols other than numbers or
     * print all borrow objects by reader id
     */
    public void showAllBorrowedByReaderId(String str) {
        var parsed = parserService.parseLong(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var reader = readerDaoJdbcImpl.findById(parsed);
        if (reader.isPresent()) {
            var borrows = borrowDaoJdbcImpl.findAll();
            borrows.stream().filter((borrow -> borrow.getReader().equals(reader.get()))).forEach(System.out::println);
        } else System.err.println("Error, this reader is not exist!");
    }

    /**
     * Return to menu if string of arguments contains any symbols other than numbers or
     * print who borrow book by book id
     */
    public void showWhoBorrowByBookId(String str) {
        var parsed = parserService.parseLong(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        var book = bookDaoJdbcImpl.findById(parsed);
        if (book.isPresent()) {
            var borrows = borrowDaoJdbcImpl.findAll().stream()
                    .filter(borrowObj -> borrowObj.getBook().equals(book.get())).toList();
            if (borrows.size() > 0) {
                var reader = borrows.get(0).getReader();
                System.out.println(reader);
            } else System.err.println("Error: this book isn`t borrowed!");
        } else System.err.println("Error, this book is not exist!");
    }

    public void showAllReadersWhitTheirBorrows() {
/*
        """SELECT r.id, r.name, b.id, b.name, author
        FROM Reader r
        LEFT JOIN Borrow bor ON r.id = bor.reader_id
        LEFT JOIN Book b ON bor.book_id = b.id"""
*/
    }

    public void showAllBooksWithTheirBorrowers() {
/*      """SELECT r.id, r.name, b.id, b.name, author
        FROM "Book" b
        LEFT JOIN "Borrow" bor ON b.id = bor.book_id
        LEFT JOIN "Reader" r ON bor.reader_id = r.id"""*/
    }
}

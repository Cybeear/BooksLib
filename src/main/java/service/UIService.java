package service;

import java.util.Scanner;

public class UIService {
    private LibraryService libraryService;
    private Scanner in;

    public LibraryService getLibraryService() {
        return libraryService;
    }

    public void setLibraryService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public Scanner getIn() {
        return in;
    }

    public void setIn(Scanner in) {
        this.in = in;
    }

    public UIService() {
        libraryService = new LibraryService();
        in = new Scanner(System.in);
    }

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
        System.out.println(str);
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
        libraryService.getAllBooks().forEach(System.out::println);
    }

    private void showAllReaders() {
        libraryService.getAllReaders().forEach(System.out::println);
    }

    private void registerReader() {
        System.out.println("Please enter new reader full name!");
        try {
            var newReaderName = in.nextLine();
            var reader = libraryService.registerReader(newReaderName);
            System.out.println(reader + " successful registered!");
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.println(illegalArgumentException.getMessage());
        }
    }

    private void addBook() {
        System.out.println("Please enter new book name and author separated by '/'. Like this: name / author!");
        try {
            var newBookData = in.nextLine();
            var book = libraryService.addBook(newBookData);
            System.out.println(book + " successful added!");
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.println("Error: Please enter new book name and author separated by '/'. Like this: name / author!");
        } catch (NullPointerException nullPointerException) {
            System.err.println("Error: " + nullPointerException.getMessage());
        }
    }

    private void borrowBook() {
        System.out.println("Please enter reader id and book id to borrow separated by '/'. Like this: 4 / 2!");
        try {
            var readeAndBookIds = in.nextLine();
            var borrow = libraryService.borrowBook(readeAndBookIds);
            if (borrow != null) {
                System.out.println(borrow);
            } else {
                System.err.println("Error: data is not exists!");
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.println(illegalArgumentException.getMessage());
        }
    }

    private void returnBook() {
        System.out.println("Please enter reader id and book id to return separated by '/'. Like this: 1 / 3!");
        try {
            var readeAndBookIds = in.nextLine();
            var returned = libraryService.returnBook(readeAndBookIds);
            if (!returned) {
                System.err.println("Data is not exists!");
            } else {
                System.out.println("Book is returned!");
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.println(illegalArgumentException.getMessage());
        }
    }

    private void showAllBorrowedByReaderId() {
        System.out.println("Please enter reader id: ");
        try {
            var readerId = in.nextLine();
            var readers = libraryService.getAllBorrowedByReaderId(readerId);
            if (!readers.isEmpty()) {
                readers.forEach(System.out::println);
            } else {
                System.err.println("Error, this reader is not exist!");
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.println(illegalArgumentException.getMessage());
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
                System.err.println("Error: this book isn`t borrowed!");
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.println(illegalArgumentException.getMessage());
        }
    }

    private void showAllReadersWithTheirBorrows() {
        var borrows = libraryService.getAllReadersWithTheirBorrows();
        if (!borrows.isEmpty()) {
            borrows.forEach(System.out::println);
        } else {
            System.err.println("Database is empty!");
        }
    }

    private void showAllBooksWithTheirBorrowers() {
        var borrows = libraryService.getAllBooksWithTheirBorrowers();
        if (!borrows.isEmpty()) {
            borrows.forEach(System.out::println);
        } else {
            System.err.println("Database is empty!");
        }
    }
}
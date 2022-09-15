package service;

import dao.BookDaoJdbcImpl;
import dao.BorrowDaoJdbcImpl;
import dao.ReaderDaoJdbcImpl;

import java.util.Scanner;

public class MenuService {
    private final LibraryService libraryService;

    public MenuService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void showMenu() {
        Scanner in = new Scanner(System.in);

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
        String option = in.nextLine();
        switch (option) {
            case "1" -> libraryService.showAllBooks();
            case "2" -> libraryService.showAllReaders();
            case "3" -> {
                System.out.println("Please enter new reader full name!");
                libraryService.registerReader(in.nextLine());
            }
            case "4" -> {
                System.out.println("Please enter new book name and author separated by '/'. Like this: name / author");
                libraryService.addBook(in.nextLine());
            }
            case "5" -> {
                System.out.println("Please enter reader id and book id to borrow separated by '/'. Like this: 4 / 2");
                libraryService.borrowABook(in.nextLine());
            }
            case "6" -> {
                System.out.println("Please enter reader id and book id to return separated by '/'. Like this: 1 / 3");
                libraryService.returnBorrowedBook(in.nextLine());
            }
            case "7" -> {
                System.out.println("Please enter reader id: ");
                libraryService.showAllBorrowedByReaderId(in.nextLine());
            }
            case "8" -> {
                System.out.println("Please enter book id: ");
                libraryService.showWhoBorrowByBookId(in.nextLine());
            }
            case "9" -> {
                libraryService.showAllReadersWithTheirBorrows();
            }
            case "10" -> {
                libraryService.showAllBooksWithTheirBorrowers();
            }
            case "exit" -> System.exit(0);
            default -> System.out.println("Program dont have this option");
        }
    }
}

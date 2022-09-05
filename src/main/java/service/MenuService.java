package service;

import dao.BookDao;
import dao.BorrowDao;
import dao.ReaderDao;

import java.util.Scanner;

public class MenuService {
    private final BookDao bookDao;
    private final ReaderDao readerDao;
    private final BorrowDao borrowDao;

    {
        bookDao = new BookDao();
        readerDao = new ReaderDao();
        borrowDao = new BorrowDao();
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
                |                             "TYPE 'exit' TO STOP THE PROGRAM AND EXIT!"                            |
                -----------------------------------------------------------------------------------------------------""";
        System.out.println(str);
        String option = in.nextLine();
        switch (option) {
            case "1" -> {
                var books = bookDao.fetchAll();
                if (books != null) books.forEach(System.out::println);
                else System.out.println("Database is empty!");
            }
            case "2" -> {
                var readers = readerDao.fetchAll();
                if (readers != null) readers.forEach(System.out::println);
                else System.out.println("Database is empty!");
            }
            case "3" -> {
                System.out.println("Please enter new reader full name!");
                if (readerDao.addNew(in.nextLine())) System.out.println("Success, new reader added!");
                else System.out.println("Error, try again!");

            }
            case "4" -> {
                System.out.println("Please enter new book name and author separated by '/'. Like this: name / author");
                if (bookDao.addNew(in.nextLine())) System.out.println("Success new book added!");
                else System.out.println("Error, try again!");
            }
            case "5" -> {
                System.out.println("Please enter reader id and book id to borrow separated by '/'. Like this: 4 / 2");
                if (borrowDao.addNew(in.nextLine())) System.out.println("Success reader borrow the book!");
                else System.out.println("Error, try again!");
            }
            case "6" -> {
                System.out.println("Please enter reader id and book id to return separated by '/'. Like this: 1 / 3");
                if (borrowDao.deleteRecord(in.nextLine()))
                    System.out.println("Success, reader return the borrowed book!");
                else System.out.println("Error, try again!");
            }
            case "7" -> {
                System.out.println("Please enter reader id: ");
                var borrows = borrowDao.fetchAllById(in.nextLine());
                if (borrows != null) borrows.forEach(System.out::println);
                else System.out.println("This reader don`t borrow a book or not exist!");
            }
            case "8" -> {
                System.out.println("Please enter book id: ");
                var borrow = borrowDao.fetchById(ParserService.parseInt(in.nextLine()));
                if (borrow != null) System.out.println(borrow.getReader());
                else System.out.println("This book is not borrowed or not exist!");
            }
            case "exit" -> System.exit(0);
            default -> System.out.println("Program dont have this option");
        }
    }
}

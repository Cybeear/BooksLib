package Core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Reader> readers = new ArrayList();
    static LinkedList<Borrow> borrows = new LinkedList<>();

    public static void main(String[] args) {
        for (var i = 0; i < 5; i++) {
            books.add(new Book(books.size(), "name" + i, "author" + i));
            readers.add(new Reader(readers.size(), "name" + i));
        }
        System.out.println("WELCOME TO THE LIBRARY!");

        while (true) {
            menu();
        }
    }

    public static void menu() {
        //Creates and print menu, User can enter any option and get callback
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
                |                             \"TYPE \'exit\' TO STOP THE PROGRAM AND EXIT!\"                            |
                -----------------------------------------------------------------------------------------------------""";
        System.out.println(str);
        String option = in.next();
        switch (option) {
            case "1" -> LibraryService.showAllBooks(books);
            case "2" -> LibraryService.showAllReaders(readers);
            case "3" -> {
                System.out.println("Please enter new reader full name!");
                in.nextLine();
                LibraryService.registerReader(readers, in.nextLine());
            }
            case "4" -> {
                System.out.println("Please enter new book name and author separated by '/'. Like this: name / author");
                in.nextLine();
                LibraryService.addBook(books, in.nextLine());
            }
            case "5" -> {
                System.out.println("Please enter reader id and book id to borrow separated by '/'. Like this: 4 / 2");
                in.nextLine();
                LibraryService.borrowABook(books, readers, borrows, in.nextLine());
            }
            case "6" -> {
                System.out.println("Please enter reader id and book id to return separated by '/'. Like this: 1 / 3");
                in.nextLine();
                LibraryService.returnBorrowedBook(books, readers, borrows, in.nextLine());
            }
            case "7" -> {
                System.out.println("Please enter reader id: ");
                in.nextLine();
                LibraryService.showAllBorrowedByReaderId(borrows, in.nextLine());
            }
            case "8" -> {
                System.out.println("Please enter book id: ");
                in.nextLine();
                LibraryService.showWhoBorrowByBookId(borrows, in.nextLine());
            }
            case "exit" -> System.exit(0);
            default -> System.out.println("Program dont have any option");
        }
    }
}

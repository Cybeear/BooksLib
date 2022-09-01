package core;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var service = new LibraryService();
        service.createData(5);
        System.out.println("WELCOME TO THE LIBRARY!");
        while (true) {
            menu(service);
        }
    }

    public static void menu(LibraryService service) {
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
        String option = in.nextLine();
        switch (option) {
            case "1" -> service.showAllBooks();
            case "2" -> service.showAllReaders();
            case "3" -> {
                System.out.println("Please enter new reader full name!");
                service.registerReader(in.nextLine());
            }
            case "4" -> {
                System.out.println("Please enter new book name and author separated by '/'. Like this: name / author");
                service.addBook(in.nextLine());
            }
            case "5" -> {
                System.out.println("Please enter reader id and book id to borrow separated by '/'. Like this: 4 / 2");
                service.borrowABook(in.nextLine());
            }
            case "6" -> {
                System.out.println("Please enter reader id and book id to return separated by '/'. Like this: 1 / 3");
                service.returnBorrowedBook(in.nextLine());
            }
            case "7" -> {
                System.out.println("Please enter reader id: ");
                service.showAllBorrowedByReaderId(in.nextLine());
            }
            case "8" -> {
                System.out.println("Please enter book id: ");
                service.showWhoBorrowByBookId(in.nextLine());
            }
            case "exit" -> System.exit(0);
            default -> System.out.println("Program dont have any option");
        }
    }
}

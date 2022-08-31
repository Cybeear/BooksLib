package Core;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        LibraryService.createData(5);
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
        String option = in.nextLine();
        switch (option) {
            case "1" -> LibraryService.showAllBooks();
            case "2" -> LibraryService.showAllReaders();
            case "3" -> LibraryService.registerReader();
            case "4" -> LibraryService.addBook();
            case "5" -> LibraryService.borrowABook();
            case "6" -> LibraryService.returnBorrowedBook();
            case "7" -> LibraryService.showAllBorrowedByReaderId();
            case "8" -> LibraryService.showWhoBorrowByBookId();
            case "exit" -> System.exit(0);
            default -> System.out.println("Program dont have any option");
        }
    }
}

package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        System.out.println("WELCOME TO THE LIBRARY!");

        while (true) {
            menu();
        }
    }

    public static void menu() {
        //Creates and print menu, User can enter any option and get callback
        Scanner in = new Scanner(System.in);
        ArrayList<Book> books = new ArrayList();
        ArrayList<Reader> readers = new ArrayList();
        for (var i = 0; i < 5; i++) {
            books.add(new Book(i, "name" + i, "author" + i));
            readers.add(new Reader(i, "name" + i));
        }

        var str = """
                -----------------------------------------------------------------------------------------------------
                | PLEASE, SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION'S NUMBER AND PRESSING ENTER KEY:  |
                |                              [1] SHOW ALL BOOKS IN THE LIBRARY                                     |
                |                              [2] SHOW ALL READERS REGISTERED IN THE LIBRARY                        |
                |                             \"TYPE \'exit\' TO STOP THE PROGRAM AND EXIT!\"                            |
                -----------------------------------------------------------------------------------------------------""";
        System.out.println(str);
        String option = in.next();

        String input;
        switch (option) {
            case "1" -> showAllBooks(books);
            case "2" -> showAllReaders(readers);
            case "exit" -> System.exit(0);
            default -> System.out.println("Program dont have any option");
        }
    }

    public static void showAllBooks(ArrayList<Book> bookArrayList) {
        bookArrayList.forEach((System.out::println));
    }

    public static void showAllReaders(ArrayList<Reader> readerArrayList) {
        readerArrayList.forEach(System.out::println);
    }
}

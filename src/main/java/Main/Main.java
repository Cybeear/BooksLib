package Main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;


public class Main {
    static ArrayList<Book> books = new ArrayList();
    static ArrayList<Reader> readers = new ArrayList();
    static ArrayList<Borrow> borrows = new ArrayList<>();

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
        String input;
        String[] inputSplit;
        switch (option) {
            case "1" -> Book.showAllBooks(books);
            case "2" -> Reader.showAllReaders(readers);
            case "3" -> registerReader(in);
            case "4" -> addBook(in);
            case "5" -> borrowABook(in);
            case "6" -> returnBorrowedBook(in);
            case "7" -> showAllBorrowed(in);
            case "8" -> showWhoBorrow(in);
            case "exit" -> System.exit(0);
            default -> System.out.println("Program dont have any option");
        }
    }

    private static void registerReader(Scanner in) {
        //Add reader to list
        System.out.println("Please enter new reader full name!");
        readers.add(new Reader(readers.size(), in.nextLine()));
    }

    public static void addBook(Scanner in) {
        //Add book to list
        System.out.println("Please enter new book name and author separated by '/'. Like this: name / author");
        in.nextLine();
        var inputSplit = in.nextLine().split("/");
        books.add(new Book(books.size(), inputSplit[0], inputSplit[1]));
    }

    private static void borrowABook(Scanner in) {
        //Add book and reader obj if exists
        System.out.println("Please enter reader id and book id to borrow separated by '/'. Like this: 4 / 2");
        in.nextLine();
        var inputSplit = in.nextLine().replace(" ", "").split("/");
        if (inputSplit[0] >= readers.size() || inputSplit[])
        borrows.add(new Borrow(books.get(Integer.parseInt(inputSplit[0])),
                readers.get(Integer.parseInt(inputSplit[1]))));
    }

    private static void returnBorrowedBook(Scanner in) {
        System.out.println("Please enter reader id and book id to return separated by '/'. Like this: 1 / 3");
        in.nextLine();
        var inputSplit = in.nextLine().replace(" ", "").split("/");
        borrows.remove(new Borrow(books.get(Integer.parseInt(inputSplit[0])),
                readers.get(Integer.parseInt(inputSplit[1]))));
    }

    private static void showAllBorrowed(Scanner in) {
        //Show all borrowed book by reader id
        System.out.println("Please enter reader id: ");
        in.nextLine();
        var parsed = parser(in.nextLine());
        if (parsed == -1 && !checkSize(parsed, borrows)){
            System.out.println("Error enter valid data!");
            return;
        }
        borrows.stream().filter(borrow -> borrow.getReader().getId() == parsed).forEach(System.out::println);
    }

    private static void showWhoBorrow(Scanner in) {
        //Show who borrow book by book id
        System.out.println();
        in.nextLine();
        var parsed = parser(in.nextLine());
        if (parsed == -1 && !checkSize(parsed, borrows)){
            System.out.println("Error enter valid data!");
            return;
        }
        borrows.stream().filter(borrow -> borrow.getBook().getId() == parsed).forEach(borrow -> System.out.println(borrow.getReader()));
    }

    private static boolean checkSize(int index, ArrayList arrayList) {
        return index < arrayList.size();
    }

    private static int parser(String str) {
        if (!checkData(str)) {
            return Integer.parseInt(str);
        }
        return -1;
    }

    private static boolean checkData(String str) {
        return str.matches("[a-zA-Z!@*&%^#+_,.()\\-\\]\\[\\\\]");
    }
}

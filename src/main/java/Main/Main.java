package Main;

import java.util.ArrayList;
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

        var books = books(5);
        var readers = readers(5);

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
        //Check if input option contains digits
        if (option.matches("\\d")) {
            //parse integer digit from string
            int nOption = Integer.parseInt(option);
            //handling all options
            switch (nOption) {
                case 1:
                    for (var i = 0; i < books.size(); i++) {
                        System.out.println("id: %s, name: %s, author: %s."
                                .formatted(i, books.get(i)[0], books.get(i)[1]));
                    }
                    break;
                case 2:
                    for (var i = 0; i < readers.size(); i++) {
                        System.out.println("id: %s, name: %s."
                                .formatted(i, readers.get(i)));
                    }
                    break;
                default:
                    System.out.println("Program dont have any option");
                    break;
            }
        } else if (option.equals("exit")) System.exit(0);
        else System.out.println("Program dont have any option");


    }

    public static ArrayList<String[]> books(int size) {
        ArrayList<String[]> bookList = new ArrayList<>();
        for (var i = 0; i < size; i++) {
            bookList.add(i, new String[]{"test" + i, "author" + i});
        }
        return bookList;
    }

    public static ArrayList<String> readers(int size) {
        ArrayList<String> readerList = new ArrayList<>();
        for (var i = 0; i < size; i++) {
            readerList.add(i, "reader" + i);
        }
        return readerList;
    }

}

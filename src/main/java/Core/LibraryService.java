package Core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LibraryService {
    public static void registerReader(ArrayList<Reader> readerArrayList, String str) {
        //Add reader to list
        readerArrayList.add(new Reader(readerArrayList.size(), str));
    }

    public static void addBook(ArrayList<Book> bookArrayList, String str) {
        //Add book to list
        var inputSplit = str.split("/");
        if (inputSplit.length < 2) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        bookArrayList.add(new Book(bookArrayList.size(),
                inputSplit[0].substring(0,inputSplit[0].length() -1),
                inputSplit[1].substring(1, inputSplit[1].length())));
    }

    public static void showAllBooks(ArrayList<Book> bookArrayList) {
        //show all books in Library
        bookArrayList.forEach((System.out::println));
    }

    public static void showAllReaders(ArrayList<Reader> readerArrayList) {
        //show all registered readers in Library
        readerArrayList.forEach(System.out::println);
    }

    public static void borrowABook(ArrayList<Book> bookArrayList, ArrayList<Reader> readerArrayList,
                                   LinkedList<Borrow> borrowLinkedList, String str) {
        //Add book and reader obj if exists
        var inputSplit = str.replace(" ", "").split("/");
        int[] parsed = {parser(inputSplit[0]), parser(inputSplit[1])};
        if ((str.equals(" ") || (parsed[0] == -1
                || parsed[1] == -1)
                || parsed[0] >= readerArrayList.size()
                || parsed[1] >= bookArrayList.size())) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        borrowLinkedList.add(new Borrow(bookArrayList.get(parsed[1]),
                readerArrayList.get(parsed[0])));
    }

    public static void returnBorrowedBook(ArrayList<Book> bookArrayList, ArrayList<Reader> readerArrayList,
                                          LinkedList<Borrow> borrowLinkedList, String str) {
        //remove book from list by reader and book id
        var inputSplit = str.replace(" ", "").split("/");
        int[] parsed = {parser(inputSplit[0]), parser(inputSplit[1])};
        if ((str.equals(" ") || (parsed[0] == -1
                || parsed[1] == -1)
                || parsed[1] >= readerArrayList.size()
                || parsed[0] >= bookArrayList.size())) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        borrowLinkedList.remove(new Borrow(bookArrayList.get(parsed[1]), readerArrayList.get(parsed[0])));
    }

    public static void showAllBorrowed(LinkedList<Borrow> borrowLinkedList, String str) {
        //Show all borrowed book by reader id
        var parsed = parser(str);
        if (parsed == -1 && !checkSize(parsed, borrowLinkedList)) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        borrowLinkedList.stream().filter(borrow -> borrow.getReader().getId() == parsed).forEach(System.out::println);
    }

    public static void showWhoBorrow(LinkedList<Borrow> borrowLinkedList, String str) {
        //Show who borrow book by book id
        var parsed = parser(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        borrowLinkedList.stream().filter(borrow -> borrow.getBook().getId() == parsed).forEach(borrow -> System.out.println(borrow.getReader()));
    }

    public static boolean checkSize(int index, List list) {
        //check size of list
        return index < list.size();
    }

    public static int parser(String str) {
        //parse int value in string
        if (checkData(str) || str.equals(" ")) {
            return -1;
        }
        return Integer.parseInt(str);
    }

    public static boolean checkData(String str) {
        //check if string match words or symbols, return boolean
        return str.matches("^[a-zA-Z!@*&%^#+_,.()\\-\\\\A-Za-z]+$");
    }

    /*public static void showAllBorrows(LinkedList<Borrow> borrowLinkedList) {
        borrowLinkedList.forEach(System.out::println);
    }*/
}

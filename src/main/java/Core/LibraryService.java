package Core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * LibraryService class used to interaction with data and objects in online Library
 */
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
                inputSplit[0].substring(0, inputSplit[0].length() - 1),
                inputSplit[1].substring(1)));
    }

    /**
     * @param bookArrayList List of books objects
     *                      show all books in the list
     */
    //have a problem with Cyrillic output
    public static void showAllBooks(ArrayList<Book> bookArrayList) {
        bookArrayList.forEach((System.out::println));
    }

    /**
     * @param readerArrayList List of readers objects
     *                        show all readers in the list
     */

    //have a problem with Cyrillic output
    public static void showAllReaders(ArrayList<Reader> readerArrayList) {
        readerArrayList.forEach(System.out::println);
    }

    /**
     * @param bookArrayList    List of books objects
     * @param readerArrayList  List of readers objects
     * @param borrowLinkedList List of borrow objects
     * @param str              string with arguments first number is a reader id, second number is a book id
     *                         function call parser function, show error message
     *                         and return to menu if string of arguments contains any characters other than numbers
     *                         add to borrowList if string not contains any characters other than numbers
     */
    public static void borrowABook(ArrayList<Book> bookArrayList, ArrayList<Reader> readerArrayList,
                                   LinkedList<Borrow> borrowLinkedList, String str) {
        //Add book and reader obj if exists
        var inputSplit = str.split(" / ");
        int[] parsed = {parser(inputSplit[0]), parser(inputSplit[1])};
        if ((str.equals(" ") || (parsed[0] == -1
                || parsed[1] == -1)
                || parsed[0] >= readerArrayList.size()
                || parsed[1] >= bookArrayList.size())) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        if (!checkIfBookBorrowed(borrowLinkedList, bookArrayList.get(parsed[1]))) {
            borrowLinkedList.add(new Borrow(bookArrayList.get(parsed[1]),
                    readerArrayList.get(parsed[0])));
            System.out.println(borrowLinkedList.getLast().toString());
        } else System.err.println("Error, this book is borrowed!");
    }

    /**
     * @param bookArrayList    List of books objects
     * @param readerArrayList  List of readers objects
     * @param borrowLinkedList List of borrow objects
     * @param str              string with arguments first number is a reader id, second number is a book id
     *                         function call parser function, show error message
     *                         and return to menu if string of arguments contains any characters other than numbers
     *                         delete object from borrowList if string not contains any characters other than numbers
     */
    public static void returnBorrowedBook(ArrayList<Book> bookArrayList, ArrayList<Reader> readerArrayList,
                                          LinkedList<Borrow> borrowLinkedList, String str) {
        var inputSplit = str.replace(" ", "").split("/");
        int[] parsed = {parser(inputSplit[0]), parser(inputSplit[1])};
        if ((str.equals(" ") || (parsed[0] == -1
                || parsed[1] == -1)
                || parsed[1] >= readerArrayList.size()
                || parsed[0] >= bookArrayList.size())) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        if (checkIfBookBorrowed(borrowLinkedList, bookArrayList.get(parsed[1]))) {
            borrowLinkedList.remove(new Borrow(bookArrayList.get(parsed[1]), readerArrayList.get(parsed[0])));
            System.out.println("Reader: " + readerArrayList.get(parsed[0]).toString()
                    + " return the book: " + bookArrayList.get(parsed[1]).toString());
        } else System.err.println("Error, this book is not borrowed!");
    }

    /**
     * @param borrowLinkedList List of borrow objects
     * @param book             Book object
     * @return boolean true if book is borrowed, use for-each from List class
     */
    private static boolean checkIfBookBorrowed(LinkedList<Borrow> borrowLinkedList, Book book) {
        for (Borrow borrow : borrowLinkedList)
            if (borrow.getBook().equals(book)) return true;
        return false;
    }

    /**
     * @param borrowLinkedList List of borrow objects
     * @param str              string with argument reader id, show error message
     *                         and return to menu if string of arguments contains any symbols other than numbers or
     *                         print all borrow objects by reader id
     */
    public static void showAllBorrowedByReaderId(LinkedList<Borrow> borrowLinkedList, String str) {
        var parsed = parser(str);
        if (parsed == -1 && !checkSize(parsed, borrowLinkedList)) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        if (checkIfDataExistsByReaderId(borrowLinkedList, parsed)) {
            borrowLinkedList.stream().filter(borrow -> borrow.getReader().getId() == parsed).forEach(System.out::println);
        } else System.out.println("This user don`t borrow a book!");
    }

    /**
     * @param borrowLinkedList List of borrow objects
     * @param str              string with argument book id, show error message
     *                         and return to menu if string of arguments contains any symbols other than numbers or
     *                         print who borrow book by book id
     */
    public static void showWhoBorrowByBookId(LinkedList<Borrow> borrowLinkedList, String str) {
        //Show who borrow book by book id
        var parsed = parser(str);
        if (parsed == -1) {
            System.err.println("Error: enter a valid data!");
            return;
        }
        if (checkIfDataExistsByBookId(borrowLinkedList, parsed))
            borrowLinkedList.stream().filter(borrow -> borrow.getBook().getId() == parsed).forEach(borrow -> System.out.println(borrow.getReader()));
        else System.out.println("This book isn`t borrowed!");
    }

    /**
     * @param borrowLinkedList List of borrow objects
     * @param id               integer number, reader id
     * @return true if reader borrow any book
     */
    private static boolean checkIfDataExistsByReaderId(LinkedList<Borrow> borrowLinkedList, int id) {
        return borrowLinkedList.stream().anyMatch(borrow -> borrow.getReader().getId() == id);
    }

    /**
     * @param borrowLinkedList List of borrow objects
     * @param id               integer number, book id
     * @return true if any reader borrow the book by this id
     */
    private static boolean checkIfDataExistsByBookId(LinkedList<Borrow> borrowLinkedList, int id) {
        return borrowLinkedList.stream().anyMatch(borrow -> borrow.getBook().getId() == id);
    }

    /**
     * @param index index of element in list
     * @param list  list of elements
     * @return boolean
     */
    public static boolean checkSize(int index, List list) {
        return index < list.size();
    }

    /**
     * @param str argument string
     * @return integer value
     */
    public static int parser(String str) {
        //parse int value in string
        if (checkData(str) || str.equals(" ")) return -1;
        return Integer.parseInt(str);
    }

    /**
     * @param str argument string
     * @return boolean if string contains any symbols other than numbers
     */
    public static boolean checkData(String str) {
        //check if string match words or symbols, return boolean
        return str.matches("^[a-zA-Z!@*&%^#+_,.()\\-\\\\A-Za-z]+$");
    }

    /*
    public static void showAllBorrows(LinkedList<Borrow> borrowLinkedList) {
        borrowLinkedList.forEach(System.out::println);
    }*/
}

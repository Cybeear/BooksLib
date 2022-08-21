package Core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class Borrow {
    private Reader reader;
    private Book book;


    public Borrow(Book books, Reader reader) {
        this.book = books;
        this.reader = reader;
    }

    public static void showAllBorrows(LinkedList<Borrow> borrowLinkedList) {
        borrowLinkedList.forEach(System.out::println);
    }

    public static void borrowABook(ArrayList<Book> bookArrayList, ArrayList<Reader> readerArrayList,
                                   LinkedList<Borrow> borrowLinkedList, String str) {
        //Add book and reader obj if exists
        var inputSplit = str.replace(" ", "").split("/");
        int[] parsed = {parser(inputSplit[0]), parser(inputSplit[1])};
        if ((str.equals(" ") || (parsed[0] == -1
                || parsed[1] == -1)
                || parsed[1] >= readerArrayList.size()
                || parsed[0] >= bookArrayList.size())) {
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

    /*
        Old function
        public static boolean checkSize(int index, ArrayList arrayList) {
            return index < arrayList.size();
        }*/
    public static boolean checkSize(int index, LinkedList linkedList) {
        return index < linkedList.size();
    }

    public static int parser(String str) {
        if (checkData(str) || str.equals(" ")) {
            return -1;
        }
        return Integer.parseInt(str);
    }

    public static boolean checkData(String str) {
        return str.matches("[a-zA-Z!@*&%^#+_,.()\\-\\]\\[\\\\A-Za-z]");
    }

    @Override
    public String toString() {
        return "Reader " + reader.toString() + " borrow the book, " + book.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrow borrow = (Borrow) o;
        return Objects.equals(reader, borrow.reader) && Objects.equals(book, borrow.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reader, book);
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}

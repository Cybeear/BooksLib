package Main;

import java.util.ArrayList;
import java.util.Objects;

public class Borrow {
    private Reader reader;
    private ArrayList<Book> books;

    public Borrow(Reader reader) {
        this.reader = reader;
    }

    public void borrowBook(Book book){
        this.books.add(book);
    }


    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Reader: \nid:" + reader.getId() + " name: " + reader.getName()
                + "\nBooks:\n ");
        books.stream().forEach(book -> stringBuilder.append(book.toString() + "\n"));
        return stringBuilder.toString();
    }


}

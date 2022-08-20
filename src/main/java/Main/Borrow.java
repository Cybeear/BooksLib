package Main;

import java.util.Objects;

public class Borrow {
    private Reader reader;
    private Book book;


    public Borrow(Book books, Reader reader) {
        this.book = books;
        this.reader = reader;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "reader=" + reader +
                ", books=" + book +
                '}';
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

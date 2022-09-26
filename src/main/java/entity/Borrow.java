package entity;

import java.util.Objects;

public class Borrow {
    private Reader reader;

    private Book book;

    /**
     * @param book   Book object field
     * @param reader Reader object field
     */
    public Borrow(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
    }

    /**
     * @return reader object
     */
    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    /**
     * @return book object
     */
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        if (reader == null) {
            return "Book: " + book + " is available!";
        } else if (book == null) {
            return "Reader: " + reader + " has no borrowed books!";
        } else {
            return "Reader: " + reader + "\t-\tborrow the book, " + book;
        }
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
}

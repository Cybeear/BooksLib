package entity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Book {
    private static AtomicLong counter = new AtomicLong(1000L);

    private long id;

    private String name;

    private String author;

    /**
     * @param name
     * @param author
     */
    public Book(String name, String author) {
        this.id = counter.incrementAndGet();
        this.name = name;
        this.author = author;
    }

    public Book(long id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    /**
     * @return book id
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return book name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return book author
     */
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(name, book.name) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author);
    }

    @Override
    public String toString() {
        return "book - id: " + id + "\tname: " + name + "\tauthor: " + author;
    }
}

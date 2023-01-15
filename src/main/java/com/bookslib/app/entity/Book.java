package com.bookslib.app.entity;

import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private static AtomicLong counter = new AtomicLong(1000L);
    private long id;
    private String name;
    private String author;

    public Book(String name, String author) {
        this.id = counter.incrementAndGet();
        this.name = name;
        this.author = author;
    }

    @Override
    public String toString() {
        return "book - id: " + id + "\tname: " + name + "\tauthor: " + author;
    }
}

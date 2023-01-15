package com.bookslib.app.entity;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Book {
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String author;

    @Override
    public String toString() {
        return "book - id: " + id + "\tname: " + name + "\tauthor: " + author;
    }
}

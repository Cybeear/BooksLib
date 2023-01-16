package com.bookslib.app.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {
    private Book book;
    private Reader reader;

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
}

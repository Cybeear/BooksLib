package com.bookslib.app.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {
    private long id;
    @Pattern(regexp = "[^0-9]*", message = "Book name must contain only letters")
    @Size(message = "Book name must be longer then 5 and less then 20 characters", min = 5, max = 20)
    @NotBlank(message = "Book name is missing")
    private String name;
    @Pattern(regexp = "[^0-9]*", message = "Book author must contain only letters")
    @Size(message = "Book author must be longer then 5 and less then 20 characters", min = 5, max = 20)
    @NotBlank(message = "Book author is missing")
    private String author;

    @Override
    public String toString() {
        return "book - id: " + id + "\tname: " + name + "\tauthor: " + author;
    }
}

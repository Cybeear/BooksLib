package com.bookslib.app.entity;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Reader {
    private long id;
    @Size(min = 5, max = 20)
    private String name;

    @Override
    public String toString() {
        return "reader - id: " + id + "\tname: " + name;
    }
}

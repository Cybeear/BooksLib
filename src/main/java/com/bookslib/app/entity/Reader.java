package com.bookslib.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Reader {
    private long id;
    private final String name;

    @Override
    public String toString() {
        return "reader - id: " + id + "\tname: " + name;
    }
}

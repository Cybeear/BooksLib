package com.bookslib.app.entity;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Reader {
    private long id;
    @NonNull
    private String name;

    @Override
    public String toString() {
        return "reader - id: " + id + "\tname: " + name;
    }
}

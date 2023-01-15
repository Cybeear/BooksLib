package com.bookslib.app.entity;

import lombok.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Reader {
    private static AtomicLong counter = new AtomicLong(1000L);
    private long id;
    private String name;

    public Reader(String name) {
        this.id = counter.incrementAndGet();
        this.name = name;
    }

    @Override
    public String toString() {
        return "reader - id: " + id + "\tname: " + name;
    }
}

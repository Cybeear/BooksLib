package entities;

import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Reader {
    private static AtomicLong counter = new AtomicLong(1000L);

    private long id;

    private String name;

    /**
     * @param name string field
     */
    public Reader(String name) {
        this.id = counter.incrementAndGet();
        this.name = name;
    }

    @Override
    public String toString() {
        return "reader - id: " + id + "\tname: " + name;
    }
}

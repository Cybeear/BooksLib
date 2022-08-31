package Core;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Reader {
    private static AtomicLong counter = new AtomicLong(1000L);

    private final long id;

    private String name;

    /**
     * @param name string field
     */
    public Reader(String name) {
        this.id = counter.incrementAndGet();
        this.name = name;
    }

    /**
     * @return reader id
     */
    public long getId() {
        return id;
    }

    /**
     * @return reader name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return id == reader.id && Objects.equals(name, reader.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "id: " + id + "\tname: " + name;
    }
}

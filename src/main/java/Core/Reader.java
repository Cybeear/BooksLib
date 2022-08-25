package Core;

import java.util.Objects;

public class Reader {

    private int id;

    private String name;

    /**
     * @param id integer field
     * @param name string field
     */
    public Reader(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return reader id
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

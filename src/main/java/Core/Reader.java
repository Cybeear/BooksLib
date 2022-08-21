package Core;

import java.util.ArrayList;
import java.util.Objects;

public class Reader {
    private int id;
    private String name;

    public static void showAllReaders(ArrayList<Reader> readerArrayList) {
        readerArrayList.forEach(System.out::println);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "id: " + id + "\tname: " + name;
    }

    public Reader(int id, String name) {
        this.id = id;
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
}

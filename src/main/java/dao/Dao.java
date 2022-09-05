package dao;

import java.util.List;

public interface Dao {
    List fetchAll();

    Object fetchById(int id);

    boolean addNew(String str);

    boolean deleteRecord(String str);
}
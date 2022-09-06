package dao;

import java.util.List;

public interface Dao {
    List fetchAll();

    Object fetchById(long id);

    boolean addNew(Object obj);

    boolean deleteRecord(Object obj);
}
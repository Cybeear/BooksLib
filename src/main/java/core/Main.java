package core;

import dao.BookDaoJdbcImpl;
import dao.BorrowDaoJdbcImpl;
import dao.ReaderDaoJdbcImpl;
import service.LibraryService;
import service.MenuService;
import service.ParserService;

public class Main {
    public static void main(String[] args) {
        var libraryService = new LibraryService(new BookDaoJdbcImpl(), new ReaderDaoJdbcImpl(), new BorrowDaoJdbcImpl(), new ParserService());
        var menu = new MenuService(libraryService);
        System.out.println("WELCOME TO THE LIBRARY!");
        while (true) {
            menu.showMenu();
        }
    }
}

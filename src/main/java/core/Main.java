package core;

import service.MenuService;

public class Main {
    public static void main(String[] args) {
        var menu = new MenuService();
        System.out.println("WELCOME TO THE LIBRARY!");
        while (true) {
            menu.showMenu();
        }
    }
}

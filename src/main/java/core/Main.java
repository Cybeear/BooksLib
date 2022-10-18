package core;

import service.UIService;

public class Main {
    public static void main(String[] args) {
        var menu = new UIService();
        System.out.println("WELCOME TO THE LIBRARY!");
        while (true) {
            menu.showMenu();
        }
    }
}
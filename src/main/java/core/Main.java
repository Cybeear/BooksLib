package core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("config")
public class Main {
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);

    }
}

//Deprecated
/*
public class Main {
    public static void main(String[] args) {
        var menu = new UIService();
        System.out.println("WELCOME TO THE LIBRARY!");
        while (true) {
            menu.showMenu();
        }
    }
}*/

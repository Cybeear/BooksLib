package core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("api")
@ComponentScan("dao")
@ComponentScan("service")
@PropertySource("classpath:application.yml")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }
}
package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
@ComponentScan("controllers")
@ComponentScan("repositories")
@ComponentScan("services")
@ComponentScan("entities")
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Autowired
    Environment environment;

    private final String URL = "spring.datasource.url";
    private final String USERNAME = "spring.datasource.username";
    private final String PASSWORD = "spring.datasource.password";
    private final String DRIVER = "spring.datasource.driver-class-name";


    @Bean
    DataSource dataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
        driverManagerDataSource.setUrl(environment.getProperty(URL));
        driverManagerDataSource.setUsername(environment.getProperty(USERNAME));
        driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
        return driverManagerDataSource;
    }
}

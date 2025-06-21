package site.javatech.cim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Главный класс приложения ЦИМ.
 * Запускает Spring Boot приложение.
 */
@SpringBootApplication
@ComponentScan(basePackages = "site.javatech.cim.core")
public class CimApplication {
    /**
     * Точка входа приложения.
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(CimApplication.class, args);
    }
}
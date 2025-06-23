package site.javatech.cim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Главный класс приложения ЦИМ.
 * Запускает Spring Boot приложение.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "site.javatech.cim.core",
        "site.javatech.cim.repository",
        "site.javatech.cim.bbb",
        "site.javatech.cim.cimmodel",
        "site.javatech.cim.requirements",
        "site.javatech.cim.status"
})
public class CimApplication {
    /**
     * Точка входа приложения.
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(CimApplication.class, args);
    }
}
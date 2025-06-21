package site.javatech.cim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения для управления цифровыми информационными моделями (ЦИМ).
 * Запускает Spring Boot приложение и инициализирует контекст.
 */
@SpringBootApplication
public class CimApplication {
    public static void main(String[] args) {
        SpringApplication.run(CimApplication.class, args);
    }
}
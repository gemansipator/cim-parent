package site.javatech.cim.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность глобальных настроек приложения (регистрация, автоодобрение).
 * Одна запись на всю систему.
 */
@Entity
@Table(name = "app_settings")
@Getter
@Setter
public class AppSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean registrationEnabled = true; // Разрешение регистрации

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean autoApprovalEnabled = true; // Автоодобрение новых пользователей (кроме первого)
}
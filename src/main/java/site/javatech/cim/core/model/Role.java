package site.javatech.cim.core.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель роли пользователя приложения ЦИМ.
 * Хранит информацию о роли (Admin, Moderator, User).
 */
@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
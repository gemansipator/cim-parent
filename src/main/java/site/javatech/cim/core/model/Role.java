package site.javatech.cim.core.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель роли приложения ЦИМ.
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
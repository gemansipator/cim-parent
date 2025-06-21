package site.javatech.cim.requirements.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель требования для модуля cim-requirements.
 * Хранит информацию о требовании (заглушка для NocoDB).
 */
@Data
@Entity
@Table(name = "requirements")
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;
}
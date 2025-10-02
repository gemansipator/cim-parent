package site.javatech.cim.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель данных для модуля приложения ЦИМ.
 * Хранит информацию о модуле, включая его название и описание.
 */
@Data
@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;
}
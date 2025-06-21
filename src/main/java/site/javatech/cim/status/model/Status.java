package site.javatech.cim.status.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель статуса для модуля cim-status.
 * Хранит информацию о статусе (заглушка для NocoDB).
 */
@Data
@Entity
@Table(name = "statuses")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;
}
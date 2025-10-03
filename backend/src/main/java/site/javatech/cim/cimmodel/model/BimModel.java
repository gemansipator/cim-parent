package site.javatech.cim.cimmodel.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель BIM-модели для модуля cim-model.
 * Хранит информацию о BIM-модели (например, IFC-файл).
 */
@Data
@Entity
@Table(name = "bim_models")
public class BimModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String filePath; // Путь к IFC-файлу (заглушка для web-ifc-viewer)
}
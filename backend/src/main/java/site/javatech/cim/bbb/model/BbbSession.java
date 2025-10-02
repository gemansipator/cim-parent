package site.javatech.cim.bbb.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель сессии BigBlueButton для модуля bbb.
 * Хранит информацию о видеоконференции.
 */
@Data
@Entity
@Table(name = "bbb_sessions")
public class BbbSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String meetingId;

    @Column
    private String name;

    @Column
    private String joinUrl; // URL для присоединения к сессии
}
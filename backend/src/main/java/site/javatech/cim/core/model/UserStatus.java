/**
 * Модель статуса пользователя (онлайн/оффлайн).
 * Хранит информацию о последнем времени активности.
 */
package site.javatech.cim.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_statuses")
@Getter
@Setter
public class UserStatus {

    @Id
    private Long userId;

    @Column(nullable = false)
    private boolean online;

    @Column(nullable = false)
    private LocalDateTime lastActive;
}
package site.javatech.cim.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.core.model.AppSettings;

import java.util.Optional;

/**
 * Репозиторий для глобальных настроек приложения.
 */
public interface AppSettingsRepository extends JpaRepository<AppSettings, Long> {
    Optional<AppSettings> findFirstByOrderByIdAsc(); // Получить единственную запись
}
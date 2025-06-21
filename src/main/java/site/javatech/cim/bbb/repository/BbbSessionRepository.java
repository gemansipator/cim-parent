package site.javatech.cim.bbb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.bbb.model.BbbSession;

/**
 * Репозиторий для работы с сущностью BbbSession в базе данных.
 */
public interface BbbSessionRepository extends JpaRepository<BbbSession, Long> {
}
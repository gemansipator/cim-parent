package site.javatech.cim.status.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.status.model.Status;

/**
 * Репозиторий для работы с сущностью Status в базе данных.
 */
public interface StatusRepository extends JpaRepository<Status, Long> {
}
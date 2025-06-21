package site.javatech.cim.requirements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.requirements.model.Requirement;

/**
 * Репозиторий для работы с сущностью Requirement в базе данных.
 */
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
}
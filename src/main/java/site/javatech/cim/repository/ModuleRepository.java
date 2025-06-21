package site.javatech.cim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.model.Module;

/**
 * Репозиторий для работы с сущностью Module в базе данных.
 */
public interface ModuleRepository extends JpaRepository<Module, Long> {
}
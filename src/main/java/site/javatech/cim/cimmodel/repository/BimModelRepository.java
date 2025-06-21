package site.javatech.cim.cimmodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.cimmodel.model.BimModel;

/**
 * Репозиторий для работы с сущностью BimModel в базе данных.
 */
public interface BimModelRepository extends JpaRepository<BimModel, Long> {
}
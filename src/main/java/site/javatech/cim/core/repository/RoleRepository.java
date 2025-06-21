package site.javatech.cim.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.javatech.cim.core.model.Role;

import java.util.Optional;

/**
 * Репозиторий для работы с ролями.
 * Предоставляет методы для доступа к данным ролей в базе данных.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Найти роль по имени.
     * @param name Имя роли
     * @return Роль, если найдена
     */
    Optional<Role> findByName(String name);
}
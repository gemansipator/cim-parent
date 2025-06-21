package site.javatech.cim.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.core.model.Role;

import java.util.Optional;

/**
 * Репозиторий для работы с ролями.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Найти роль по имени.
     * @param name Имя роли
     * @return Optional с ролью или пустой, если не найдена
     */
    Optional<Role> findByName(String name);
}
package site.javatech.cim.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.core.model.User;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Найти пользователя по имени.
     * @param username Имя пользователя
     * @return Optional с пользователем или пустой, если не найден
     */
    Optional<User> findByUsername(String username);
}
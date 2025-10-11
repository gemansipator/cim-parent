package site.javatech.cim.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.core.model.User;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Добавлен метод existsByUsername для проверки существования пользователя по имени.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username); // Добавлено для проверки существования
}
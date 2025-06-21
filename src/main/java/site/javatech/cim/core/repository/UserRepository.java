package site.javatech.cim.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.javatech.cim.core.model.User;

/**
 * Репозиторий для работы с пользователями.
 * Предоставляет методы для доступа к данным пользователей в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Найти пользователя по имени.
     * @param username Имя пользователя
     * @return Пользователь
     */
    User findByUsername(String username);
}
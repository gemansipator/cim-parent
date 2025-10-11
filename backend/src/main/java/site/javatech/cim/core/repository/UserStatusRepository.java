/**
 * Репозиторий для работы со статусами пользователей в базе данных.
 */
package site.javatech.cim.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javatech.cim.core.model.UserStatus;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
}
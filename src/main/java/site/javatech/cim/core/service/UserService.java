package site.javatech.cim.core.service;

import site.javatech.cim.core.model.User;

import java.util.List;
import java.util.Set;

/**
 * Интерфейс сервиса для управления пользователями приложения ЦИМ.
 */
public interface UserService {
    /**
     * Создает нового пользователя с указанными ролями.
     * @param user данные пользователя
     * @param roleNames список имен ролей
     * @return созданный пользователь
     */
    User createUser(User user, Set<String> roleNames);

    /**
     * Получает список всех пользователей.
     * @return список пользователей
     */
    List<User> getAllUsers();

    /**
     * Получает пользователя по ID.
     * @param id идентификатор пользователя
     * @return пользователь или null, если не найден
     */
    User getUserById(Long id);

    /**
     * Назначает роли пользователю (доступно только администратору).
     * @param userId идентификатор пользователя
     * @param roleNames список имен ролей
     * @return обновленный пользователь
     */
    User assignRoles(Long userId, Set<String> roleNames);
}
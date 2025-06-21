package site.javatech.cim.core.service;

import site.javatech.cim.core.model.User;

import java.util.List;
import java.util.Set;

/**
 * Сервис для управления пользователями.
 */
public interface UserService {
    /**
     * Создать нового пользователя.
     * @param user Данные пользователя
     * @param roleNames Список имен ролей
     * @return Созданный пользователь
     */
    User createUser(User user, Set<String> roleNames);

    /**
     * Получить список всех пользователей.
     * @return Список пользователей
     */
    List<User> getAllUsers();

    /**
     * Получить пользователя по ID.
     * @param id ID пользователя
     * @return Пользователь или null, если не найден
     */
    User getUserById(Long id);

    /**
     * Получить пользователя по имени.
     * @param username Имя пользователя
     * @return Пользователь или null, если не найден
     */
    User getUserByUsername(String username);

    /**
     * Назначить роли пользователю.
     * @param userId ID пользователя
     * @param roleNames Список имен ролей
     * @return Обновленный пользователь
     */
    User assignRoles(Long userId, Set<String> roleNames);
}
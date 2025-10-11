/**
 * Интерфейс сервиса для управления пользователями.
 * Добавлены методы для модерации и проверки настроек.
 * Добавлен метод getUserIdByUsername для чата.
 */
package site.javatech.cim.core.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    User createUser(Map<String, Object> userData);

    User createUser(User user, List<String> roleNames);

    User assignRoles(Long id, Set<String> roleNames);

    boolean existsByUsername(String username);

    List<Role> getRolesByUsername(String username);

    // Добавлено для модерации
    User approveUser(Long id);

    User blockUser(Long id);

    User unblockUser(Long id);

    void deleteUser(Long id);

    // Добавлено для смены роли
    User updateRole(Long id, String roleName);

    // Добавлено для проверки глобальных настроек
    boolean isRegistrationEnabled();

    boolean isAutoApprovalEnabled();

    // Добавлено для чата
    Long getUserIdByUsername(String username);
}
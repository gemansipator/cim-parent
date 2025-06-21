package site.javatech.cim.core.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Интерфейс сервиса для управления пользователями.
 */
public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    User createUser(Map<String, Object> userData);

    User createUser(User user, Set<String> roleNames);

    User assignRoles(Long id, Set<String> roleNames);

    boolean existsByUsername(String username);

    List<Role> getRolesByUsername(String username);
}
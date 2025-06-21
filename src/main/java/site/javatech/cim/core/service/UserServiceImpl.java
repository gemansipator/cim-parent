package site.javatech.cim.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.model.User;
import site.javatech.cim.core.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Реализация сервиса для управления пользователями.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    /**
     * Создать нового пользователя.
     * @param user Данные пользователя
     * @param roleNames Список имен ролей
     * @return Созданный пользователь
     */
    @Override
    public User createUser(User user, Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleService.getRoleByName(roleName);
            if (role == null) {
                throw new RuntimeException("Роль " + roleName + " не найдена");
            }
            roles.add(role);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    /**
     * Получить список всех пользователей.
     * @return Список пользователей
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получить пользователя по ID.
     * @param id ID пользователя
     * @return Пользователь или null, если не найден
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Получить пользователя по имени.
     * @param username Имя пользователя
     * @return Пользователь или null, если не найден
     */
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Назначить роли пользователю.
     * @param userId ID пользователя
     * @param roleNames Список имен ролей
     * @return Обновленный пользователь
     */
    @Override
    public User assignRoles(Long userId, Set<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + userId + " не найден"));
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleService.getRoleByName(roleName);
            if (role == null) {
                throw new RuntimeException("Роль " + roleName + " не найдена");
            }
            roles.add(role);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }
}
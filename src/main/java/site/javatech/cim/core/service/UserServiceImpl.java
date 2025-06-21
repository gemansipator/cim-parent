package site.javatech.cim.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.model.User;
import site.javatech.cim.core.repository.RoleRepository;
import site.javatech.cim.core.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления пользователями и аутентификации.
 * Предоставляет функционал для работы с пользователями и их ролями, а также загрузки данных для аутентификации.
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Получить список всех пользователей.
     * @return Список пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получить пользователя по идентификатору.
     * @param id Идентификатор пользователя
     * @return Пользователь или null, если не найден
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Получить пользователя по имени.
     * @param username Имя пользователя
     * @return Пользователь
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Создать нового пользователя.
     * @param user Данные пользователя
     * @param roleNames Список имен ролей
     * @return Созданный пользователь
     */
    public User createUser(User user, Set<String> roleNames) {
        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Роль не найдена: " + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    /**
     * Назначить роли пользователю.
     * @param id Идентификатор пользователя
     * @param roleNames Список имен ролей
     * @return Обновленный пользователь или null, если не найден
     */
    public User assignRoles(Long id, Set<String> roleNames) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Роль не найдена: " + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    /**
     * Загрузить данные пользователя для аутентификации.
     * @param username Имя пользователя
     * @return Данные пользователя для Spring Security
     * @throws UsernameNotFoundException Если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                .build();
    }
}
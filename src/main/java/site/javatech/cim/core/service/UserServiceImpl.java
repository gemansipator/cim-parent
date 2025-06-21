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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Реализация сервиса для управления пользователями.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Создать нового пользователя.
     * @param user Данные пользователя
     * @param roleNames Список имен ролей
     * @return Созданный пользователь
     */
    @Override
    public User createUser(User user, Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        if (roleNames != null) {
            for (String roleName : roleNames) {
                roleRepository.findByName(roleName).ifPresent(roles::add);
            }
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
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            roleRepository.findByName(roleName).ifPresent(roles::add);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    /**
     * Загрузить пользователя по имени для аутентификации.
     * @param username Имя пользователя
     * @return UserDetails объект
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                .build();
    }
}
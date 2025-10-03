package site.javatech.cim.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.model.User;
import site.javatech.cim.core.repository.RoleRepository;
import site.javatech.cim.core.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления пользователями и аутентификации.
 * Предоставляет функционал для работы с пользователями и их ролями, а также загрузки данных для аутентификации.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Получить список всех пользователей.
     * @return Список пользователей
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получить пользователя по идентификатору.
     * @param id Идентификатор пользователя
     * @return Пользователь или null, если не найден
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Получить пользователя по имени.
     * @param username Имя пользователя
     * @return Пользователь
     * @throws UsernameNotFoundException Если пользователь не найден
     */
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    /**
     * Создать нового пользователя.
     * Первый зарегистрировавшийся пользователь получает роль ADMIN, остальные — роли из roleNames.
     * @param userData Данные пользователя (user: {username, password}, roleNames: список ролей)
     * @return Созданный пользователь
     * @throws IllegalArgumentException Если данные некорректны
     */
    @Override
    @Transactional
    public User createUser(Map<String, Object> userData) {
        @SuppressWarnings("unchecked")
        Map<String, String> userMap = (Map<String, String>) userData.get("user");
        String username = userMap.get("username");
        String password = userMap.get("password");
        @SuppressWarnings("unchecked")
        List<String> roleNames = (List<String>) userData.get("roleNames");

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        List<Role> roles = new ArrayList<>();
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Роль ADMIN не найдена"));
            roles.add(adminRole);
        } else {
            roles = roleNames.stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseGet(() -> {
                                Role newRole = new Role();
                                newRole.setName(roleName);
                                return roleRepository.save(newRole);
                            }))
                    .collect(Collectors.toList());
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    /**
     * Создать нового пользователя.
     * @param user Данные пользователя
     * @param roleNames Список имен ролей
     * @return Созданный пользователь
     * @throws RuntimeException Если пользователь уже существует или роль не найдена
     */
    @Override
    @Transactional
    public User createUser(User user, Set<String> roleNames) {
        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Роль не найдена: " + roleName)))
                .collect(Collectors.toList());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    /**
     * Назначить роли пользователю.
     * @param id Идентификатор пользователя
     * @param roleNames Список имен ролей
     * @return Обновленный пользователь или null, если не найден
     * @throws RuntimeException Если роль не найдена
     */
    @Override
    @Transactional
    public User assignRoles(Long id, Set<String> roleNames) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Роль не найдена: " + roleName)))
                .collect(Collectors.toList());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    /**
     * Проверка существования пользователя по имени.
     * @param username Имя пользователя
     * @return true, если пользователь существует
     */
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Получение ролей пользователя по имени.
     * @param username Имя пользователя
     * @return Список ролей
     * @throws UsernameNotFoundException Если пользователь не найден
     */
    @Override
    public List<Role> getRolesByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getRoles)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    /**
     * Загрузка данных пользователя для аутентификации.
     * @param username Имя пользователя
     * @return Данные пользователя для Spring Security
     * @throws UsernameNotFoundException Если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .map(name -> name.startsWith("ROLE_") ? name.substring(5) : name)
                        .toArray(String[]::new))
                .build();
    }
}
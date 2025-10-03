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
 * Добавлена логика модерации: статус PENDING для новых, кроме первого (админ).
 * Методы approveUser, blockUser, unblockUser, deleteUser для админа.
 * Интеграция с AppSettings для глобальных флагов.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppSettingsService appSettingsService; // Добавлено для глобальных настроек

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
     * Первый зарегистрировавшийся пользователь получает роль ADMIN и статус APPROVED.
     * Остальные — статус PENDING и роли из roleNames (если autoApprovalEnabled = true, то APPROVED).
     * Проверка глобальных настроек: если registrationEnabled = false, отклонить.
     * @param userData Данные пользователя (user: {username, password}, roleNames: список ролей)
     * @return Созданный пользователь
     * @throws IllegalArgumentException Если данные некорректны или регистрация закрыта
     */
    @Override
    @Transactional
    public User createUser(Map<String, Object> userData) {
        // Проверка глобальных настроек
        if (!appSettingsService.isRegistrationEnabled()) {
            throw new IllegalArgumentException("Регистрация временно закрыта администратором");
        }

        @SuppressWarnings("unchecked")
        Map<String, String> userMap = (Map<String, String>) userData.get("user");
        String username = userMap.get("username");
        String password = userMap.get("password");
        @SuppressWarnings("unchecked")
        List<String> roleNames = (List<String>) userData.get("roleNames");

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с именем " + username + " уже существует");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        List<Role> roles = new ArrayList<>();
        if (userRepository.count() == 0) {
            // Первый пользователь — админ
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ADMIN");
                        return roleRepository.save(newRole);
                    });
            roles.add(adminRole);
            user.setStatus(User.Status.APPROVED); // Автоматическое одобрение
        } else {
            // Остальные — PENDING или APPROVED, в зависимости от autoApprovalEnabled
            user.setStatus(appSettingsService.isAutoApprovalEnabled() ? User.Status.APPROVED : User.Status.PENDING);
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
     * Одобрить пользователя (изменить статус на APPROVED).
     * @param id Идентификатор пользователя
     * @return Обновленный пользователь
     * @throws IllegalArgumentException Если пользователь не найден
     */
    @Transactional
    public User approveUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setStatus(User.Status.APPROVED);
        return userRepository.save(user);
    }

    /**
     * Заблокировать пользователя (изменить статус на BLOCKED).
     * @param id Идентификатор пользователя
     * @return Обновленный пользователь
     * @throws IllegalArgumentException Если пользователь не найден
     */
    @Transactional
    public User blockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setStatus(User.Status.BLOCKED);
        return userRepository.save(user);
    }

    /**
     * Разблокировать пользователя (изменить статус на APPROVED).
     * @param id Идентификатор пользователя
     * @return Обновленный пользователь
     * @throws IllegalArgumentException Если пользователь не найден
     */
    @Transactional
    public User unblockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setStatus(User.Status.APPROVED);
        return userRepository.save(user);
    }

    /**
     * Удалить пользователя.
     * @param id Идентификатор пользователя
     * @throws IllegalArgumentException Если пользователь не найден
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        userRepository.delete(user);
    }

    /**
     * Создать нового пользователя (ручное добавление админом).
     * @param user Данные пользователя
     * @param roleNames Список имен ролей
     * @return Созданный пользователь
     * @throws RuntimeException Если пользователь уже существует или роль не найдена
     */
    @Override
    @Transactional
    public User createUser(User user, Set<String> roleNames) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с именем " + user.getUsername() + " уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(User.Status.APPROVED); // Ручное добавление — сразу одобрено
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
        return userRepository.existsByUsername(username);
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
     * @throws UsernameNotFoundException Если пользователь не найден или заблокирован
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        // Проверка статуса при логине
        if (user.getStatus() == User.Status.PENDING) {
            throw new UsernameNotFoundException("Ваш аккаунт ожидает одобрения администратором");
        }
        if (user.getStatus() == User.Status.BLOCKED) {
            throw new UsernameNotFoundException("Ваш аккаунт заблокирован администратором");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .map(name -> name.startsWith("ROLE_") ? name.substring(5) : name)
                        .toArray(String[]::new))
                .build();
    }

    /**
     * Проверка разрешения регистрации (из глобальных настроек).
     * @return true, если регистрация разрешена
     */
    @Override
    public boolean isRegistrationEnabled() {
        return appSettingsService.isRegistrationEnabled();
    }

    /**
     * Проверка автоодобрения (из глобальных настроек).
     * @return true, если автоодобрение включено
     */
    @Override
    public boolean isAutoApprovalEnabled() {
        return appSettingsService.isAutoApprovalEnabled();
    }
}
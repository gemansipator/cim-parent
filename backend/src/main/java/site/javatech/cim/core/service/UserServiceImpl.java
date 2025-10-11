/**
 * Реализация сервиса для управления пользователями и аутентификации.
 * Добавлена логика модерации: статус PENDING для новых, кроме первого (админ).
 * Методы approveUser, blockUser, unblockUser, deleteUser для админа.
 * Интеграция с AppSettings для глобальных флагов.
 * Добавлен метод getUserIdByUsername для чата.
 */
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

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppSettingsService appSettingsService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    @Override
    @Transactional
    public User createUser(Map<String, Object> userData) {
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
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ADMIN");
                        return roleRepository.save(newRole);
                    });
            roles.add(adminRole);
            user.setStatus(User.Status.APPROVED);
        } else {
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

    @Transactional
    public User approveUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setStatus(User.Status.APPROVED);
        return userRepository.save(user);
    }

    @Transactional
    public User blockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setStatus(User.Status.BLOCKED);
        return userRepository.save(user);
    }

    @Transactional
    public User unblockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setStatus(User.Status.APPROVED);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User createUser(User user, List<String> roleNames) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с именем " + user.getUsername() + " уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(User.Status.APPROVED);
        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(roleName);
                            return roleRepository.save(newRole);
                        }))
                .collect(Collectors.toList());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User assignRoles(Long id, Set<String> roleNames) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(roleName);
                            return roleRepository.save(newRole);
                        }))
                .collect(Collectors.toList());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Transactional
    public User updateRole(Long id, String roleName) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Role role = roleRepository.findByName(roleName).orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName(roleName);
            return roleRepository.save(newRole);
        });
        user.setRoles(List.of(role));
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<Role> getRolesByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getRoles)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        if (user.getStatus() == User.Status.PENDING) {
            throw new UsernameNotFoundException("Дождитесь одобрения Вашей учетной записи");
        }
        if (user.getStatus() == User.Status.BLOCKED) {
            throw new UsernameNotFoundException("Ваша учетная запись заблокирована");
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

    @Override
    public boolean isRegistrationEnabled() {
        return appSettingsService.isRegistrationEnabled();
    }

    @Override
    public boolean isAutoApprovalEnabled() {
        return appSettingsService.isAutoApprovalEnabled();
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }
}
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
 * Реализация сервиса для управления пользователями приложения ЦИМ.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

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
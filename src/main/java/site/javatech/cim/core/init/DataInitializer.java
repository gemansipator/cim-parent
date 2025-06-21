package site.javatech.cim.core.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Компонент для инициализации данных приложения ЦИМ при старте.
 * Создает фиксированные роли (Admin, Moderator, User), если они отсутствуют в базе данных.
 */
@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        List<String> roleNames = Arrays.asList("Admin", "Moderator", "User");
        for (String roleName : roleNames) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
}
package site.javatech.cim.core.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.repository.RoleRepository;

/**
 * Инициализатор данных для создания начальных ролей в базе данных.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Создает начальные роли при запуске приложения.
     * @param args аргументы командной строки
     * @throws Exception если произошла ошибка
     */
    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("User").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("User");
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName("Admin").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("Admin");
            roleRepository.save(adminRole);
        }
    }
}
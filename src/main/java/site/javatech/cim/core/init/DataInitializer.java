package site.javatech.cim.core.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.repository.RoleRepository;

/**
 * Инициализатор данных для создания начальных ролей в базе данных.
 * Очищает таблицы roles, user_roles, users и сбрасывает автоинкремент перед созданием ролей.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Очищает таблицы roles, user_roles, users, сбрасывает автоинкремент и создаёт роли ADMIN, SUPERUSER, USER.
     * @param args Аргументы командной строки
     * @throws Exception Если произошла ошибка
     */
    @Override
    public void run(String... args) throws Exception {
        // Очистка таблиц и сброс автоинкремента
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
        jdbcTemplate.update("ALTER SEQUENCE roles_id_seq RESTART WITH 1");
        jdbcTemplate.update("ALTER SEQUENCE users_id_seq RESTART WITH 1");

        // Создание ролей
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole); // id=1
        }
        if (roleRepository.findByName("SUPERUSER").isEmpty()) {
            Role superUserRole = new Role();
            superUserRole.setName("SUPERUSER");
            roleRepository.save(superUserRole); // id=2
        }
        if (roleRepository.findByName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole); // id=3
        }
    }
}
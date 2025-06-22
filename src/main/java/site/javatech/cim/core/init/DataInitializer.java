package site.javatech.cim.core.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.repository.RoleRepository;

/**
 * Инициализатор данных для создания начальных ролей и очистки таблиц.
 * Очищает таблицы roles, user_roles и users (при необходимости), сбрасывает автоинкремент
 * и создаёт роли ADMIN, SUPERUSER, USER.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Очищает таблицы, сбрасывает автоинкремент и создаёт роли ADMIN, SUPERUSER, USER.
     * Сбрасывает users_id_seq, если таблица users пуста или содержит пустые username.
     * @param args Аргументы командной строки
     * @throws Exception Если произошла ошибка
     */
    @Override
    public void run(String... args) throws Exception {
        // Проверка состояния таблицы users
        Long userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class);
        Long invalidUsers = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username IS NULL OR username = ''", Long.class);

        // Очистка таблиц и сброс автоинкремента
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM roles");
        jdbcTemplate.update("ALTER SEQUENCE roles_id_seq RESTART WITH 1");

        if (userCount == 0 || invalidUsers > 0) {
            jdbcTemplate.update("DELETE FROM users");
            jdbcTemplate.update("ALTER SEQUENCE users_id_seq RESTART WITH 1");
        }

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
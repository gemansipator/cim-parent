package site.javatech.cim.core.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import site.javatech.cim.model.Module;
import site.javatech.cim.repository.ModuleRepository;
import site.javatech.cim.core.repository.RoleRepository;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Проверка состояния таблицы users
        Long userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class);
        Long invalidUsers = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username IS NULL OR username = ''", Long.class);

        // Очистка таблиц и сброс автоинкремента (если нужно)
        if (userCount == 0 || invalidUsers > 0) {
            jdbcTemplate.update("DELETE FROM user_roles");
            jdbcTemplate.update("DELETE FROM users");
            jdbcTemplate.update("ALTER SEQUENCE users_id_seq RESTART WITH 1");
        }

        // Инициализация ролей с фиксированными ID
        initRoles();

        // Инициализация модулей
        initModules();
    }

    private void initRoles() {
        // Удаляем только если нет пользователей (чтобы не сломать связи)
        Long userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class);
        if (userCount == 0) {
            jdbcTemplate.update("DELETE FROM roles");
            jdbcTemplate.update("ALTER SEQUENCE roles_id_seq RESTART WITH 1");
        }

        // Создаем/обновляем роли с фиксированными ID
        saveRoleWithFixedId(1L, "ADMIN");
        saveRoleWithFixedId(2L, "SUPERUSER");
        saveRoleWithFixedId(3L, "USER");
    }

    private void saveRoleWithFixedId(Long id, String name) {
        // Используем native query для гарантированной вставки с нужным ID
        jdbcTemplate.update(
                "INSERT INTO roles (id, name) VALUES (?, ?) " +
                        "ON CONFLICT (id) DO UPDATE SET name = ?",
                id, name, name
        );
    }

    private void initModules() {
        // Очищаем модули только если их нет
        if (moduleRepository.count() == 0) {
            jdbcTemplate.update("DELETE FROM modules");
            jdbcTemplate.update("ALTER SEQUENCE modules_id_seq RESTART WITH 1");

            Module module1 = new Module();
            module1.setName("Модуль 1");
            module1.setDescription("Описание модуля 1");

            Module module2 = new Module();
            module2.setName("Модуль 2");
            module2.setDescription("Описание модуля 2");

            Module settingsModeration = new Module();
            settingsModeration.setName("Настройки и модерация");
            settingsModeration.setDescription("Модуль для управления настройками и модерацией");

            moduleRepository.saveAll(List.of(module1, module2, settingsModeration));
        }
    }
}
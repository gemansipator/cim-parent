package site.javatech.cim.core.service;

import site.javatech.cim.core.model.Role;

import java.util.List;

/**
 * Интерфейс сервиса для управления ролями приложения ЦИМ.
 * Поддерживает только операции чтения ролей.
 */
public interface RoleService {
    /**
     * Получает список всех ролей.
     * @return список ролей
     */
    List<Role> getAllRoles();

    /**
     * Получает роль по имени.
     * @param name имя роли
     * @return роль или null, если не найдена
     */
    Role getRoleByName(String name);
}
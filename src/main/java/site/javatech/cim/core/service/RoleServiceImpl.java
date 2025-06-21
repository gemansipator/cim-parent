package site.javatech.cim.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.javatech.cim.core.model.Role;
import site.javatech.cim.core.repository.RoleRepository;

import java.util.List;

/**
 * Реализация сервиса для управления ролями.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Получает список всех ролей.
     * @return список ролей
     */
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Получает роль по имени.
     * @param name имя роли
     * @return роль или null, если не найдена
     */
    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }
}
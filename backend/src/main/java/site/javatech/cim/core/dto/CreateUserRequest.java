package site.javatech.cim.core.dto;

import site.javatech.cim.core.model.User;

import java.util.Set;

/**
 * DTO для создания нового пользователя.
 */
public class CreateUserRequest {

    private User user;
    private Set<String> roleNames;

    /**
     * Получить данные пользователя.
     * @return Данные пользователя
     */
    public User getUser() {
        return user;
    }

    /**
     * Установить данные пользователя.
     * @param user Данные пользователя
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Получить список имен ролей.
     * @return Список имен ролей
     */
    public Set<String> getRoleNames() {
        return roleNames;
    }

    /**
     * Установить список имен ролей.
     * @param roleNames Список имен ролей
     */
    public void setRoleNames(Set<String> roleNames) {
        this.roleNames = roleNames;
    }
}
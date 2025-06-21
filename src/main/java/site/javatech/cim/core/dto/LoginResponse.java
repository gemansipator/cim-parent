package site.javatech.cim.core.dto;

import site.javatech.cim.core.model.User;

/**
 * DTO для ответа на запрос аутентификации.
 */
public class LoginResponse {

    private User user;
    private String token;

    /**
     * Конструктор.
     * @param user Пользователь
     * @param token JWT-токен
     */
    public LoginResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    /**
     * Получить данные пользователя.
     * @return Пользователь
     */
    public User getUser() {
        return user;
    }

    /**
     * Установить данные пользователя.
     * @param user Пользователь
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Получить JWT-токен.
     * @return JWT-токен
     */
    public String getToken() {
        return token;
    }

    /**
     * Установить JWT-токен.
     * @param token JWT-токен
     */
    public void setToken(String token) {
        this.token = token;
    }
}
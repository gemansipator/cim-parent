package site.javatech.cim.core.dto;

/**
 * DTO для запроса аутентификации пользователя.
 */
public class LoginRequest {

    private String username;
    private String password;

    /**
     * Получить имя пользователя.
     * @return Имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Установить имя пользователя.
     * @param username Имя пользователя
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получить пароль.
     * @return Пароль
     */
    public String getPassword() {
        return password;
    }

    /**
     * Установить пароль.
     * @param password Пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
package site.javatech.cim.core.model;

import jakarta.persistence.*;
import java.util.Set;

/**
 * Модель роли пользователя.
 * Хранит информацию о роли и связанных с ней пользователях.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    /**
     * Получить идентификатор роли.
     * @return Идентификатор роли
     */
    public Long getId() {
        return id;
    }

    /**
     * Установить идентификатор роли.
     * @param id Идентификатор роли
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить имя роли.
     * @return Имя роли
     */
    public String getName() {
        return name;
    }

    /**
     * Установить имя роли.
     * @param name Имя роли
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить список пользователей с данной ролью.
     * @return Список пользователей
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Установить список пользователей с данной ролью.
     * @param users Список пользователей
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
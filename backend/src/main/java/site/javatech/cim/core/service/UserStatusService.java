/**
 * Интерфейс сервиса для управления статусами пользователей.
 */
package site.javatech.cim.core.service;

import site.javatech.cim.core.model.UserStatus;

import java.util.List;

public interface UserStatusService {
    void updateUserStatus(Long userId, boolean online);

    List<UserStatus> getAllUserStatuses();
}
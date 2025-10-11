/**
 * Реализация сервиса для управления статусами пользователей.
 */
package site.javatech.cim.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.javatech.cim.core.model.UserStatus;
import site.javatech.cim.core.repository.UserStatusRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserStatusServiceImpl implements UserStatusService {

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Override
    public void updateUserStatus(Long userId, boolean online) {
        UserStatus status = userStatusRepository.findById(userId)
                .orElse(new UserStatus());
        status.setUserId(userId);
        status.setOnline(online);
        status.setLastActive(LocalDateTime.now());
        userStatusRepository.save(status);
    }

    @Override
    public List<UserStatus> getAllUserStatuses() {
        return userStatusRepository.findAll();
    }
}
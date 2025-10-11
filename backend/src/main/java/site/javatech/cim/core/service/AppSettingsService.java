package site.javatech.cim.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.javatech.cim.core.model.AppSettings;
import site.javatech.cim.core.repository.AppSettingsRepository;

import java.util.Optional; // Добавлено для Optional

/**
 * Сервис для управления глобальными настройками приложения.
 */
@Service
public class AppSettingsService {

    @Autowired
    private AppSettingsRepository appSettingsRepository;

    /**
     * Получить глобальные настройки.
     * @return Настройки (создаёт, если не существует)
     */
    public AppSettings getSettings() {
        Optional<AppSettings> optionalSettings = appSettingsRepository.findFirstByOrderByIdAsc();
        return optionalSettings.orElseGet(() -> {
            AppSettings settings = new AppSettings();
            settings.setRegistrationEnabled(true);
            settings.setAutoApprovalEnabled(true);
            return appSettingsRepository.save(settings);
        });
    }

    /**
     * Обновить глобальные настройки.
     * @param settings Настройки
     * @return Обновлённые настройки
     */
    @Transactional
    public AppSettings updateSettings(AppSettings settings) {
        AppSettings current = getSettings();
        current.setRegistrationEnabled(settings.isRegistrationEnabled());
        current.setAutoApprovalEnabled(settings.isAutoApprovalEnabled());
        return appSettingsRepository.save(current);
    }

    /**
     * Проверка разрешения регистрации.
     * @return true, если регистрация разрешена
     */
    public boolean isRegistrationEnabled() {
        return getSettings().isRegistrationEnabled();
    }

    /**
     * Проверка автоодобрения.
     * @return true, если автоодобрение включено
     */
    public boolean isAutoApprovalEnabled() {
        return getSettings().isAutoApprovalEnabled();
    }
}
package site.javatech.cim.bbb.service;

import site.javatech.cim.bbb.model.BbbSession;

import java.util.List;

/**
 * Интерфейс сервиса для управления сессиями BigBlueButton в модуле bbb.
 */
public interface BbbSessionService {
    /**
     * Создает новую сессию BBB.
     * @param bbbSession данные сессии
     * @return созданная сессия
     */
    BbbSession createBbbSession(BbbSession bbbSession);

    /**
     * Получает список всех сессий BBB.
     * @return список сессий
     */
    List<BbbSession> getAllBbbSessions();

    /**
     * Получает сессию BBB по ID.
     * @param id идентификатор сессии
     * @return сессия или null, если не найдена
     */
    BbbSession getBbbSessionById(Long id);
}
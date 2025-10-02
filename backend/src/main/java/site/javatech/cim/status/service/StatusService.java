package site.javatech.cim.status.service;

import site.javatech.cim.status.model.Status;

import java.util.List;

/**
 * Интерфейс сервиса для управления статусами в модуле cim-status.
 */
public interface StatusService {
    /**
     * Создает новый статус.
     * @param status данные статуса
     * @return созданный статус
     */
    Status createStatus(Status status);

    /**
     * Получает список всех статусов.
     * @return список статусов
     */
    List<Status> getAllStatuses();

    /**
     * Получает статус по ID.
     * @param id идентификатор статуса
     * @return статус или null, если не найден
     */
    Status getStatusById(Long id);
}
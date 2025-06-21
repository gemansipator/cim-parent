package site.javatech.cim.requirements.service;

import site.javatech.cim.requirements.model.Requirement;

import java.util.List;

/**
 * Интерфейс сервиса для управления требованиями в модуле cim-requirements.
 */
public interface RequirementService {
    /**
     * Создает новое требование.
     * @param requirement данные требования
     * @return созданное требование
     */
    Requirement createRequirement(Requirement requirement);

    /**
     * Получает список всех требований.
     * @return список требований
     */
    List<Requirement> getAllRequirements();

    /**
     * Получает требование по ID.
     * @param id идентификатор требования
     * @return требование или null, если не найдено
     */
    Requirement getRequirementById(Long id);
}
package site.javatech.cim.cimmodel.service;

import site.javatech.cim.cimmodel.model.BimModel;

import java.util.List;

/**
 * Интерфейс сервиса для управления BIM-моделями в модуле cim-model.
 */
public interface BimModelService {
    /**
     * Создает новую BIM-модель.
     * @param bimModel данные модели
     * @return созданная модель
     */
    BimModel createBimModel(BimModel bimModel);

    /**
     * Получает список всех BIM-моделей.
     * @return список моделей
     */
    List<BimModel> getAllBimModels();

    /**
     * Получает BIM-модель по ID.
     * @param id идентификатор модели
     * @return модель или null, если не найдена
     */
    BimModel getBimModelById(Long id);
}
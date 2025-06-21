package site.javatech.cim.service;

import site.javatech.cim.model.Module;
import java.util.List;

/**
 * Интерфейс сервиса для управления модулями приложения ЦИМ.
 */
public interface ModuleService {
    /**
     * Получает список всех модулей.
     * @return список модулей
     */
    List<Module> getAllModules();

    /**
     * Создает новый модуль.
     * @param module данные модуля
     * @return созданный модуль
     */
    Module createModule(Module module);

    /**
     * Получает модуль по идентификатору.
     * @param id идентификатор модуля
     * @return модуль или null, если не найден
     */
    Module getModuleById(Long id);
}
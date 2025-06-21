package site.javatech.cim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.javatech.cim.model.Module;
import site.javatech.cim.repository.ModuleRepository;

import java.util.List;

/**
 * Реализация сервиса для управления модулями приложения ЦИМ.
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    @Override
    public Module createModule(Module module) {
        return moduleRepository.save(module);
    }

    @Override
    public Module getModuleById(Long id) {
        return moduleRepository.findById(id).orElse(null);
    }
}
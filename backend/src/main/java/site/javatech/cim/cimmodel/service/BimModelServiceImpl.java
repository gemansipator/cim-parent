package site.javatech.cim.cimmodel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.javatech.cim.cimmodel.model.BimModel;
import site.javatech.cim.cimmodel.repository.BimModelRepository;

import java.util.List;

/**
 * Реализация сервиса для управления BIM-моделями в модуле cim-model.
 */
@Service
public class BimModelServiceImpl implements BimModelService {

    @Autowired
    private BimModelRepository bimModelRepository;

    @Override
    public BimModel createBimModel(BimModel bimModel) {
        return bimModelRepository.save(bimModel);
    }

    @Override
    public List<BimModel> getAllBimModels() {
        return bimModelRepository.findAll();
    }

    @Override
    public BimModel getBimModelById(Long id) {
        return bimModelRepository.findById(id).orElse(null);
    }
}
package site.javatech.cim.requirements.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.javatech.cim.requirements.model.Requirement;
import site.javatech.cim.requirements.repository.RequirementRepository;

import java.util.List;

/**
 * Реализация сервиса для управления требованиями в модуле cim-requirements.
 */
@Service
public class RequirementServiceImpl implements RequirementService {

    @Autowired
    private RequirementRepository requirementRepository;

    @Override
    public Requirement createRequirement(Requirement requirement) {
        return requirementRepository.save(requirement);
    }

    @Override
    public List<Requirement> getAllRequirements() {
        return requirementRepository.findAll();
    }

    @Override
    public Requirement getRequirementById(Long id) {
        return requirementRepository.findById(id).orElse(null);
    }
}
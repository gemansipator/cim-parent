package site.javatech.cim.status.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.javatech.cim.status.model.Status;
import site.javatech.cim.status.repository.StatusRepository;

import java.util.List;

/**
 * Реализация сервиса для управления статусами в модуле cim-status.
 */
@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public Status createStatus(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    @Override
    public Status getStatusById(Long id) {
        return statusRepository.findById(id).orElse(null);
    }
}
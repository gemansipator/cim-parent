package site.javatech.cim.bbb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.javatech.cim.bbb.model.BbbSession;
import site.javatech.cim.bbb.repository.BbbSessionRepository;

import java.util.List;

/**
 * Реализация сервиса для управления сессиями BigBlueButton в модуле bbb.
 */
@Service
public class BbbSessionServiceImpl implements BbbSessionService {

    @Autowired
    private BbbSessionRepository bbbSessionRepository;

    @Override
    public BbbSession createBbbSession(BbbSession bbbSession) {
        return bbbSessionRepository.save(bbbSession);
    }

    @Override
    public List<BbbSession> getAllBbbSessions() {
        return bbbSessionRepository.findAll();
    }

    @Override
    public BbbSession getBbbSessionById(Long id) {
        return bbbSessionRepository.findById(id).orElse(null);
    }
}
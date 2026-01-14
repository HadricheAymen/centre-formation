package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.SessionPedagogique;
import spring.jpa.centre_formation.exception.BusinessException;
import spring.jpa.centre_formation.exception.ResourceNotFoundException;
import spring.jpa.centre_formation.repository.SessionPedagogiqueRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service métier pour la gestion des sessions pédagogiques
 */
@Service
@Transactional
public class SessionPedagogiqueService {
    
    private final SessionPedagogiqueRepository sessionRepository;
    
    @Autowired
    public SessionPedagogiqueService(SessionPedagogiqueRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    
    /**
     * Créer une nouvelle session pédagogique
     */
    public SessionPedagogique creerSession(SessionPedagogique session) {
        // Vérifier si le code existe déjà
        if (sessionRepository.existsByCode(session.getCode())) {
            throw new BusinessException("Une session avec le code " + session.getCode() + " existe déjà");
        }
        
        // Vérifier que la date de début est avant la date de fin
        if (session.getDateDebut().isAfter(session.getDateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }
        
        return sessionRepository.save(session);
    }
    
    /**
     * Récupérer toutes les sessions
     */
    @Transactional(readOnly = true)
    public List<SessionPedagogique> getAllSessions() {
        return sessionRepository.findAll();
    }
    
    /**
     * Récupérer une session par son ID
     */
    @Transactional(readOnly = true)
    public SessionPedagogique getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SessionPedagogique", "id", id));
    }
    
    /**
     * Récupérer une session par son code
     */
    @Transactional(readOnly = true)
    public SessionPedagogique getSessionByCode(String code) {
        return sessionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("SessionPedagogique", "code", code));
    }
    
    /**
     * Récupérer les sessions actives
     */
    @Transactional(readOnly = true)
    public List<SessionPedagogique> getSessionsActives() {
        return sessionRepository.findByActive(true);
    }
    
    /**
     * Récupérer la session active à une date donnée
     */
    @Transactional(readOnly = true)
    public SessionPedagogique getSessionActive(LocalDate date) {
        return sessionRepository.findSessionActive(date)
                .orElseThrow(() -> new ResourceNotFoundException("Aucune session active trouvée pour la date " + date));
    }
    
    /**
     * Mettre à jour une session
     */
    public SessionPedagogique updateSession(Long id, SessionPedagogique sessionDetails) {
        SessionPedagogique session = getSessionById(id);
        
        // Vérifier si le nouveau code n'existe pas déjà
        if (!session.getCode().equals(sessionDetails.getCode()) &&
            sessionRepository.existsByCode(sessionDetails.getCode())) {
            throw new BusinessException("Une session avec le code " + sessionDetails.getCode() + " existe déjà");
        }
        
        // Vérifier que la date de début est avant la date de fin
        if (sessionDetails.getDateDebut().isAfter(sessionDetails.getDateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }
        
        session.setCode(sessionDetails.getCode());
        session.setNom(sessionDetails.getNom());
        session.setDateDebut(sessionDetails.getDateDebut());
        session.setDateFin(sessionDetails.getDateFin());
        session.setActive(sessionDetails.getActive());
        
        return sessionRepository.save(session);
    }
    
    /**
     * Supprimer une session
     */
    public void deleteSession(Long id) {
        SessionPedagogique session = getSessionById(id);
        sessionRepository.delete(session);
    }
    
    /**
     * Activer/Désactiver une session
     */
    public SessionPedagogique toggleActive(Long id) {
        SessionPedagogique session = getSessionById(id);
        session.setActive(!session.getActive());
        return sessionRepository.save(session);
    }
}


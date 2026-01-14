package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.Cours;
import spring.jpa.centre_formation.entity.Formateur;
import spring.jpa.centre_formation.entity.Groupe;
import spring.jpa.centre_formation.entity.Specialite;
import spring.jpa.centre_formation.entity.SessionPedagogique;
import spring.jpa.centre_formation.exception.BusinessException;
import spring.jpa.centre_formation.exception.ResourceNotFoundException;
import spring.jpa.centre_formation.repository.CoursRepository;
import spring.jpa.centre_formation.repository.FormateurRepository;
import spring.jpa.centre_formation.repository.GroupeRepository;
import spring.jpa.centre_formation.repository.SpecialiteRepository;
import spring.jpa.centre_formation.repository.SessionPedagogiqueRepository;

import java.util.List;

/**
 * Service métier pour la gestion des cours
 */
@Service
@Transactional
public class CoursService {
    
    private final CoursRepository coursRepository;
    private final FormateurRepository formateurRepository;
    private final SpecialiteRepository specialiteRepository;
    private final SessionPedagogiqueRepository sessionRepository;
    private final GroupeRepository groupeRepository;
    
    @Autowired
    public CoursService(CoursRepository coursRepository,
                       FormateurRepository formateurRepository,
                       SpecialiteRepository specialiteRepository,
                       SessionPedagogiqueRepository sessionRepository,
                       GroupeRepository groupeRepository) {
        this.coursRepository = coursRepository;
        this.formateurRepository = formateurRepository;
        this.specialiteRepository = specialiteRepository;
        this.sessionRepository = sessionRepository;
        this.groupeRepository = groupeRepository;
    }
    
    /**
     * Créer un nouveau cours
     */
    public Cours creerCours(Cours cours) {
        // Vérifier si le code existe déjà
        if (coursRepository.existsByCode(cours.getCode())) {
            throw new BusinessException("Un cours avec le code " + cours.getCode() + " existe déjà");
        }
        
        return coursRepository.save(cours);
    }
    
    /**
     * Récupérer tous les cours
     */
    @Transactional(readOnly = true)
    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }
    
    /**
     * Récupérer un cours par son ID
     */
    @Transactional(readOnly = true)
    public Cours getCoursById(Long id) {
        return coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours", "id", id));
    }
    
    /**
     * Récupérer un cours par son code
     */
    @Transactional(readOnly = true)
    public Cours getCoursByCode(String code) {
        return coursRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cours", "code", code));
    }
    
    /**
     * Mettre à jour un cours
     */
    public Cours updateCours(Long id, Cours coursDetails) {
        Cours cours = getCoursById(id);
        
        // Vérifier si le nouveau code n'existe pas déjà
        if (!cours.getCode().equals(coursDetails.getCode()) &&
            coursRepository.existsByCode(coursDetails.getCode())) {
            throw new BusinessException("Un cours avec le code " + coursDetails.getCode() + " existe déjà");
        }
        
        cours.setCode(coursDetails.getCode());
        cours.setTitre(coursDetails.getTitre());
        cours.setDescription(coursDetails.getDescription());
        cours.setNombreHeures(coursDetails.getNombreHeures());
        cours.setCoefficient(coursDetails.getCoefficient());
        
        return coursRepository.save(cours);
    }
    
    /**
     * Supprimer un cours
     */
    public void deleteCours(Long id) {
        Cours cours = getCoursById(id);
        coursRepository.delete(cours);
    }
    
    /**
     * Rechercher des cours par mot-clé
     */
    @Transactional(readOnly = true)
    public List<Cours> searchCours(String keyword) {
        return coursRepository.searchByKeyword(keyword);
    }
    
    /**
     * Récupérer les cours d'un formateur
     */
    @Transactional(readOnly = true)
    public List<Cours> getCoursByFormateur(Long formateurId) {
        return coursRepository.findByFormateurId(formateurId);
    }
    
    /**
     * Récupérer les cours d'une spécialité
     */
    @Transactional(readOnly = true)
    public List<Cours> getCoursBySpecialite(Long specialiteId) {
        return coursRepository.findBySpecialiteId(specialiteId);
    }
    
    /**
     * Récupérer les cours d'une session
     */
    @Transactional(readOnly = true)
    public List<Cours> getCoursBySession(Long sessionId) {
        return coursRepository.findBySessionId(sessionId);
    }
    
    /**
     * Récupérer les cours d'un groupe
     */
    @Transactional(readOnly = true)
    public List<Cours> getCoursByGroupe(Long groupeId) {
        return coursRepository.findByGroupeId(groupeId);
    }
    
    /**
     * Affecter un formateur à un cours
     */
    public Cours affecterFormateur(Long coursId, Long formateurId) {
        Cours cours = getCoursById(coursId);
        Formateur formateur = formateurRepository.findById(formateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Formateur", "id", formateurId));
        
        cours.setFormateur(formateur);
        return coursRepository.save(cours);
    }
    
    /**
     * Affecter une spécialité à un cours
     */
    public Cours affecterSpecialite(Long coursId, Long specialiteId) {
        Cours cours = getCoursById(coursId);
        Specialite specialite = specialiteRepository.findById(specialiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialite", "id", specialiteId));
        
        cours.setSpecialite(specialite);
        return coursRepository.save(cours);
    }
    
    /**
     * Affecter une session à un cours
     */
    public Cours affecterSession(Long coursId, Long sessionId) {
        Cours cours = getCoursById(coursId);
        SessionPedagogique session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("SessionPedagogique", "id", sessionId));
        
        cours.setSession(session);
        return coursRepository.save(cours);
    }
    
    /**
     * Affecter un groupe à un cours
     */
    public Cours affecterGroupe(Long coursId, Long groupeId) {
        Cours cours = getCoursById(coursId);
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe", "id", groupeId));
        
        if (!cours.getGroupes().contains(groupe)) {
            cours.getGroupes().add(groupe);
        }
        
        return coursRepository.save(cours);
    }
    
    /**
     * Retirer un groupe d'un cours
     */
    public Cours retirerGroupe(Long coursId, Long groupeId) {
        Cours cours = getCoursById(coursId);
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe", "id", groupeId));
        
        cours.getGroupes().remove(groupe);
        return coursRepository.save(cours);
    }
    
    /**
     * Compter le nombre d'inscriptions à un cours
     */
    @Transactional(readOnly = true)
    public Long countInscriptions(Long coursId) {
        return coursRepository.countInscriptionsByCoursId(coursId);
    }
    
    /**
     * Calculer la moyenne d'un cours
     */
    @Transactional(readOnly = true)
    public Double calculerMoyenneCours(Long coursId) {
        Double moyenne = coursRepository.calculerMoyenneCours(coursId);
        return moyenne != null ? moyenne : 0.0;
    }
    
    /**
     * Calculer le taux de réussite d'un cours
     */
    @Transactional(readOnly = true)
    public Double calculerTauxReussite(Long coursId) {
        Double taux = coursRepository.calculerTauxReussite(coursId);
        return taux != null ? taux : 0.0;
    }
}


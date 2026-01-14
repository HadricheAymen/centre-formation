package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.Cours;
import spring.jpa.centre_formation.entity.Seance;
import spring.jpa.centre_formation.exception.BusinessException;
import spring.jpa.centre_formation.exception.ResourceNotFoundException;
import spring.jpa.centre_formation.repository.CoursRepository;
import spring.jpa.centre_formation.repository.SeanceRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service métier pour la gestion des séances
 */
@Service
@Transactional
public class SeanceService {
    
    private final SeanceRepository seanceRepository;
    private final CoursRepository coursRepository;
    
    @Autowired
    public SeanceService(SeanceRepository seanceRepository, CoursRepository coursRepository) {
        this.seanceRepository = seanceRepository;
        this.coursRepository = coursRepository;
    }
    
    /**
     * Créer une nouvelle séance
     */
    public Seance creerSeance(Seance seance) {
        // Vérifier que le cours existe
        Cours cours = coursRepository.findById(seance.getCours().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cours", "id", seance.getCours().getId()));
        
        // Vérifier que la date de début est avant la date de fin
        if (seance.getDateDebut().isAfter(seance.getDateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }
        
        // Vérifier les conflits de salle
        List<Seance> conflitsSalle = seanceRepository.findConflitsSalle(
            seance.getSalle(), seance.getDateDebut(), seance.getDateFin());
        if (!conflitsSalle.isEmpty()) {
            throw new BusinessException("La salle " + seance.getSalle() + 
                " est déjà occupée à cet horaire");
        }
        
        // Vérifier les conflits de formateur
        if (cours.getFormateur() != null) {
            List<Seance> conflitsFormateur = seanceRepository.findConflitsFormateur(
                cours.getFormateur().getId(), seance.getDateDebut(), seance.getDateFin());
            if (!conflitsFormateur.isEmpty()) {
                throw new BusinessException("Le formateur a déjà une séance à cet horaire");
            }
        }
        
        return seanceRepository.save(seance);
    }
    
    /**
     * Récupérer toutes les séances
     */
    @Transactional(readOnly = true)
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }
    
    /**
     * Récupérer une séance par son ID
     */
    @Transactional(readOnly = true)
    public Seance getSeanceById(Long id) {
        return seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seance", "id", id));
    }
    
    /**
     * Récupérer les séances d'un cours
     */
    @Transactional(readOnly = true)
    public List<Seance> getSeancesByCours(Long coursId) {
        return seanceRepository.findByCoursId(coursId);
    }
    
    /**
     * Récupérer les séances non annulées d'un cours
     */
    @Transactional(readOnly = true)
    public List<Seance> getSeancesActivesByCours(Long coursId) {
        return seanceRepository.findByCoursIdAndAnnulee(coursId, false);
    }
    
    /**
     * Récupérer les séances d'une salle
     */
    @Transactional(readOnly = true)
    public List<Seance> getSeancesBySalle(String salle) {
        return seanceRepository.findBySalle(salle);
    }
    
    /**
     * Récupérer les séances d'une période
     */
    @Transactional(readOnly = true)
    public List<Seance> getSeancesByPeriode(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return seanceRepository.findByPeriode(dateDebut, dateFin);
    }
    
    /**
     * Récupérer l'emploi du temps d'un formateur
     */
    @Transactional(readOnly = true)
    public List<Seance> getEmploiDuTempsFormateur(Long formateurId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return seanceRepository.findEmploiDuTempsFormateur(formateurId, dateDebut, dateFin);
    }
    
    /**
     * Récupérer l'emploi du temps d'un étudiant
     */
    @Transactional(readOnly = true)
    public List<Seance> getEmploiDuTempsEtudiant(Long etudiantId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return seanceRepository.findEmploiDuTempsEtudiant(etudiantId, dateDebut, dateFin);
    }
    
    /**
     * Mettre à jour une séance
     */
    public Seance updateSeance(Long id, Seance seanceDetails) {
        Seance seance = getSeanceById(id);
        
        // Vérifier que la date de début est avant la date de fin
        if (seanceDetails.getDateDebut().isAfter(seanceDetails.getDateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }
        
        // Vérifier les conflits de salle si la salle ou les horaires changent
        if (!seance.getSalle().equals(seanceDetails.getSalle()) ||
            !seance.getDateDebut().equals(seanceDetails.getDateDebut()) ||
            !seance.getDateFin().equals(seanceDetails.getDateFin())) {
            
            List<Seance> conflitsSalle = seanceRepository.findConflitsSalle(
                seanceDetails.getSalle(), seanceDetails.getDateDebut(), seanceDetails.getDateFin());
            conflitsSalle.removeIf(s -> s.getId().equals(id)); // Exclure la séance actuelle
            
            if (!conflitsSalle.isEmpty()) {
                throw new BusinessException("La salle " + seanceDetails.getSalle() + 
                    " est déjà occupée à cet horaire");
            }
        }
        
        seance.setDateDebut(seanceDetails.getDateDebut());
        seance.setDateFin(seanceDetails.getDateFin());
        seance.setSalle(seanceDetails.getSalle());
        seance.setType(seanceDetails.getType());
        seance.setDescription(seanceDetails.getDescription());
        
        return seanceRepository.save(seance);
    }
    
    /**
     * Annuler une séance
     */
    public Seance annulerSeance(Long id) {
        Seance seance = getSeanceById(id);
        seance.setAnnulee(true);
        return seanceRepository.save(seance);
    }
    
    /**
     * Supprimer une séance
     */
    public void deleteSeance(Long id) {
        Seance seance = getSeanceById(id);
        seanceRepository.delete(seance);
    }
}


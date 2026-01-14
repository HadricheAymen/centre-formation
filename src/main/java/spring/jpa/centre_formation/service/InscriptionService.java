package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.Cours;
import spring.jpa.centre_formation.entity.Etudiant;
import spring.jpa.centre_formation.entity.Inscription;
import spring.jpa.centre_formation.exception.BusinessException;
import spring.jpa.centre_formation.exception.ResourceNotFoundException;
import spring.jpa.centre_formation.repository.CoursRepository;
import spring.jpa.centre_formation.repository.EtudiantRepository;
import spring.jpa.centre_formation.repository.InscriptionRepository;

import java.util.List;

/**
 * Service métier pour la gestion des inscriptions
 */
@Service
@Transactional
public class InscriptionService {
    
    private final InscriptionRepository inscriptionRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;
    private final EmailService emailService;

    @Autowired
    public InscriptionService(InscriptionRepository inscriptionRepository,
                             EtudiantRepository etudiantRepository,
                             CoursRepository coursRepository,
                             EmailService emailService) {
        this.inscriptionRepository = inscriptionRepository;
        this.etudiantRepository = etudiantRepository;
        this.coursRepository = coursRepository;
        this.emailService = emailService;
    }
    
    /**
     * Inscrire un étudiant à un cours
     */
    public Inscription inscrireEtudiant(Long etudiantId, Long coursId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant", "id", etudiantId));

        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours", "id", coursId));

        // Vérifier si l'étudiant est déjà inscrit
        if (inscriptionRepository.existsByEtudiantIdAndCoursIdAndActive(etudiantId, coursId, true)) {
            throw new BusinessException("L'étudiant est déjà inscrit à ce cours");
        }

        Inscription inscription = new Inscription(etudiant, cours);
        Inscription savedInscription = inscriptionRepository.save(inscription);

        // Envoyer les notifications par email
        emailService.envoyerEmailInscription(savedInscription);
        emailService.notifierFormateurNouvelleInscription(savedInscription);

        return savedInscription;
    }
    
    /**
     * Récupérer toutes les inscriptions
     */
    @Transactional(readOnly = true)
    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }
    
    /**
     * Récupérer une inscription par son ID
     */
    @Transactional(readOnly = true)
    public Inscription getInscriptionById(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription", "id", id));
    }
    
    /**
     * Récupérer les inscriptions d'un étudiant
     */
    @Transactional(readOnly = true)
    public List<Inscription> getInscriptionsByEtudiant(Long etudiantId) {
        return inscriptionRepository.findByEtudiantId(etudiantId);
    }
    
    /**
     * Récupérer les inscriptions actives d'un étudiant
     */
    @Transactional(readOnly = true)
    public List<Inscription> getInscriptionsActivesByEtudiant(Long etudiantId) {
        return inscriptionRepository.findByEtudiantIdAndActive(etudiantId, true);
    }
    
    /**
     * Récupérer les inscriptions d'un cours
     */
    @Transactional(readOnly = true)
    public List<Inscription> getInscriptionsByCours(Long coursId) {
        return inscriptionRepository.findByCoursId(coursId);
    }
    
    /**
     * Récupérer les inscriptions actives d'un cours
     */
    @Transactional(readOnly = true)
    public List<Inscription> getInscriptionsActivesByCours(Long coursId) {
        return inscriptionRepository.findByCoursIdAndActive(coursId, true);
    }
    
    /**
     * Annuler une inscription
     */
    public Inscription annulerInscription(Long inscriptionId) {
        Inscription inscription = getInscriptionById(inscriptionId);

        if (!inscription.getActive()) {
            throw new BusinessException("Cette inscription est déjà annulée");
        }

        inscription.annuler();
        Inscription savedInscription = inscriptionRepository.save(inscription);

        // Notifier le formateur de la désinscription
        emailService.notifierFormateurDesinscription(savedInscription);

        return savedInscription;
    }
    
    /**
     * Annuler l'inscription d'un étudiant à un cours
     */
    public Inscription annulerInscriptionEtudiantCours(Long etudiantId, Long coursId) {
        Inscription inscription = inscriptionRepository.findActiveInscription(etudiantId, coursId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Aucune inscription active trouvée pour cet étudiant et ce cours"));

        inscription.annuler();
        Inscription savedInscription = inscriptionRepository.save(inscription);

        // Notifier le formateur de la désinscription
        emailService.notifierFormateurDesinscription(savedInscription);

        return savedInscription;
    }
    
    /**
     * Supprimer une inscription
     */
    public void deleteInscription(Long id) {
        Inscription inscription = getInscriptionById(id);
        inscriptionRepository.delete(inscription);
    }
    
    /**
     * Compter les inscriptions actives d'un cours
     */
    @Transactional(readOnly = true)
    public Long countInscriptionsActives(Long coursId) {
        return inscriptionRepository.countActiveInscriptionsByCoursId(coursId);
    }
}


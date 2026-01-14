package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.Formateur;
import spring.jpa.centre_formation.entity.Specialite;
import spring.jpa.centre_formation.exception.ResourceNotFoundException;
import spring.jpa.centre_formation.repository.FormateurRepository;
import spring.jpa.centre_formation.repository.SpecialiteRepository;

import java.util.List;

/**
 * Service métier pour la gestion des formateurs
 */
@Service
@Transactional
public class FormateurService {
    
    private final FormateurRepository formateurRepository;
    private final SpecialiteRepository specialiteRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public FormateurService(FormateurRepository formateurRepository,
                           SpecialiteRepository specialiteRepository,
                           PasswordEncoder passwordEncoder) {
        this.formateurRepository = formateurRepository;
        this.specialiteRepository = specialiteRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Créer un nouveau formateur
     */
    public Formateur creerFormateur(Formateur formateur) {
        // Encoder le mot de passe
        formateur.setPassword(passwordEncoder.encode(formateur.getPassword()));
        return formateurRepository.save(formateur);
    }
    
    /**
     * Récupérer tous les formateurs
     */
    @Transactional(readOnly = true)
    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }
    
    /**
     * Récupérer un formateur par son ID
     */
    @Transactional(readOnly = true)
    public Formateur getFormateurById(Long id) {
        return formateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formateur", "id", id));
    }
    
    /**
     * Mettre à jour un formateur
     */
    public Formateur updateFormateur(Long id, Formateur formateurDetails) {
        Formateur formateur = getFormateurById(id);
        
        formateur.setNom(formateurDetails.getNom());
        formateur.setPrenom(formateurDetails.getPrenom());
        formateur.setEmail(formateurDetails.getEmail());
        formateur.setTelephone(formateurDetails.getTelephone());
        formateur.setBiographie(formateurDetails.getBiographie());
        
        if (formateurDetails.getSpecialite() != null) {
            formateur.setSpecialite(formateurDetails.getSpecialite());
        }
        
        return formateurRepository.save(formateur);
    }
    
    /**
     * Supprimer un formateur
     */
    public void deleteFormateur(Long id) {
        Formateur formateur = getFormateurById(id);
        formateurRepository.delete(formateur);
    }
    
    /**
     * Rechercher des formateurs par mot-clé
     */
    @Transactional(readOnly = true)
    public List<Formateur> searchFormateurs(String keyword) {
        return formateurRepository.searchByKeyword(keyword);
    }
    
    /**
     * Récupérer les formateurs d'une spécialité
     */
    @Transactional(readOnly = true)
    public List<Formateur> getFormateursBySpecialite(Long specialiteId) {
        return formateurRepository.findBySpecialiteId(specialiteId);
    }
    
    /**
     * Affecter une spécialité à un formateur
     */
    public Formateur affecterSpecialite(Long formateurId, Long specialiteId) {
        Formateur formateur = getFormateurById(formateurId);
        Specialite specialite = specialiteRepository.findById(specialiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialite", "id", specialiteId));
        
        formateur.setSpecialite(specialite);
        return formateurRepository.save(formateur);
    }
    
    /**
     * Compter le nombre de cours d'un formateur
     */
    @Transactional(readOnly = true)
    public Long countCours(Long formateurId) {
        return formateurRepository.countCoursByFormateurId(formateurId);
    }

    /**
     * Activer/Désactiver un formateur
     */
    public Formateur toggleActive(Long formateurId) {
        Formateur formateur = getFormateurById(formateurId);
        formateur.setActive(!formateur.getActive());
        return formateurRepository.save(formateur);
    }
}


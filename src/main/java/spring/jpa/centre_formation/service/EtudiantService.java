package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.Etudiant;
import spring.jpa.centre_formation.entity.Groupe;
import spring.jpa.centre_formation.exception.BusinessException;
import spring.jpa.centre_formation.exception.ResourceNotFoundException;
import spring.jpa.centre_formation.repository.EtudiantRepository;
import spring.jpa.centre_formation.repository.GroupeRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service métier pour la gestion des étudiants
 */
@Service
@Transactional
public class EtudiantService {
    
    private final EtudiantRepository etudiantRepository;
    private final GroupeRepository groupeRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public EtudiantService(EtudiantRepository etudiantRepository, 
                          GroupeRepository groupeRepository,
                          PasswordEncoder passwordEncoder) {
        this.etudiantRepository = etudiantRepository;
        this.groupeRepository = groupeRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Créer un nouvel étudiant
     */
    public Etudiant creerEtudiant(Etudiant etudiant) {
        // Vérifier si le matricule existe déjà
        if (etudiantRepository.existsByMatricule(etudiant.getMatricule())) {
            throw new BusinessException("Un étudiant avec le matricule " + etudiant.getMatricule() + " existe déjà");
        }
        
        // Encoder le mot de passe
        etudiant.setPassword(passwordEncoder.encode(etudiant.getPassword()));
        
        // Définir la date d'inscription si non définie
        if (etudiant.getDateInscription() == null) {
            etudiant.setDateInscription(LocalDate.now());
        }
        
        return etudiantRepository.save(etudiant);
    }
    

    @Transactional(readOnly = true)
    public Etudiant getEtudiantByEmail(String email) {
        return etudiantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé avec l'email: " + email));
    }
    /**
     * Récupérer tous les étudiants
     */
    @Transactional(readOnly = true)
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }
    
    /**
     * Récupérer un étudiant par son ID
     */
    @Transactional(readOnly = true)
    public Etudiant getEtudiantById(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant", "id", id));
    }
    

    /**
     * Récupérer un étudiant par son matricule
     */
    @Transactional(readOnly = true)
    public Etudiant getEtudiantByMatricule(String matricule) {
        return etudiantRepository.findByMatricule(matricule)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant", "matricule", matricule));
    }
    
    /**
     * Mettre à jour un étudiant
     */
    public Etudiant updateEtudiant(Long id, Etudiant etudiantDetails) {
        Etudiant etudiant = getEtudiantById(id);
        
        // Vérifier si le nouveau matricule n'existe pas déjà
        if (!etudiant.getMatricule().equals(etudiantDetails.getMatricule()) &&
            etudiantRepository.existsByMatricule(etudiantDetails.getMatricule())) {
            throw new BusinessException("Un étudiant avec le matricule " + etudiantDetails.getMatricule() + " existe déjà");
        }
        
        etudiant.setMatricule(etudiantDetails.getMatricule());
        etudiant.setNom(etudiantDetails.getNom());
        etudiant.setPrenom(etudiantDetails.getPrenom());
        etudiant.setEmail(etudiantDetails.getEmail());
        etudiant.setDateNaissance(etudiantDetails.getDateNaissance());
        etudiant.setAdresse(etudiantDetails.getAdresse());
        etudiant.setTelephone(etudiantDetails.getTelephone());
        
        return etudiantRepository.save(etudiant);
    }
    
    /**
     * Supprimer un étudiant
     */
    public void deleteEtudiant(Long id) {
        Etudiant etudiant = getEtudiantById(id);
        etudiantRepository.delete(etudiant);
    }
    
    /**
     * Rechercher des étudiants par mot-clé
     */
    @Transactional(readOnly = true)
    public List<Etudiant> searchEtudiants(String keyword) {
        return etudiantRepository.searchByKeyword(keyword);
    }
    
    /**
     * Récupérer les étudiants d'un groupe
     */
    @Transactional(readOnly = true)
    public List<Etudiant> getEtudiantsByGroupe(Long groupeId) {
        return etudiantRepository.findByGroupeId(groupeId);
    }
    
    /**
     * Récupérer les étudiants inscrits à un cours
     */
    @Transactional(readOnly = true)
    public List<Etudiant> getEtudiantsByCours(Long coursId) {
        return etudiantRepository.findByCoursId(coursId);
    }
    
    /**
     * Affecter un étudiant à un groupe
     */
    public Etudiant affecterAuGroupe(Long etudiantId, Long groupeId) {
        Etudiant etudiant = getEtudiantById(etudiantId);
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe", "id", groupeId));
        
        // Vérifier la capacité du groupe
        if (groupe.getCapaciteMax() != null) {
            Long nombreEtudiants = groupeRepository.countEtudiantsByGroupeId(groupeId);
            if (nombreEtudiants >= groupe.getCapaciteMax()) {
                throw new BusinessException("Le groupe " + groupe.getNom() + " a atteint sa capacité maximale");
            }
        }
        
        if (!etudiant.getGroupes().contains(groupe)) {
            etudiant.getGroupes().add(groupe);
        }
        
        return etudiantRepository.save(etudiant);
    }
    
    /**
     * Retirer un étudiant d'un groupe
     */
    public Etudiant retirerDuGroupe(Long etudiantId, Long groupeId) {
        Etudiant etudiant = getEtudiantById(etudiantId);
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe", "id", groupeId));
        
        etudiant.getGroupes().remove(groupe);
        return etudiantRepository.save(etudiant);
    }
    
    /**
     * Calculer la moyenne générale d'un étudiant
     */
    @Transactional(readOnly = true)
    public Double calculerMoyenneGenerale(Long etudiantId) {
        Double moyenne = etudiantRepository.calculerMoyenneGenerale(etudiantId);
        return moyenne != null ? moyenne : 0.0;
    }

    /**
     * Activer/Désactiver un étudiant
     */
    public Etudiant toggleActive(Long etudiantId) {
        Etudiant etudiant = getEtudiantById(etudiantId);
        etudiant.setActive(!etudiant.getActive());
        return etudiantRepository.save(etudiant);
    }
}


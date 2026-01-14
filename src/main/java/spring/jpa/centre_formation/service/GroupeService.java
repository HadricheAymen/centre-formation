package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.Groupe;
import spring.jpa.centre_formation.exception.BusinessException;
import spring.jpa.centre_formation.exception.ResourceNotFoundException;
import spring.jpa.centre_formation.repository.GroupeRepository;

import java.util.List;

/**
 * Service métier pour la gestion des groupes
 */
@Service
@Transactional
public class GroupeService {
    
    private final GroupeRepository groupeRepository;
    
    @Autowired
    public GroupeService(GroupeRepository groupeRepository) {
        this.groupeRepository = groupeRepository;
    }
    
    /**
     * Créer un nouveau groupe
     */
    public Groupe creerGroupe(Groupe groupe) {
        // Vérifier si le code existe déjà
        if (groupeRepository.existsByCode(groupe.getCode())) {
            throw new BusinessException("Un groupe avec le code " + groupe.getCode() + " existe déjà");
        }
        
        return groupeRepository.save(groupe);
    }
    
    /**
     * Récupérer tous les groupes
     */
    @Transactional(readOnly = true)
    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAll();
    }
    
    /**
     * Récupérer un groupe par son ID
     */
    @Transactional(readOnly = true)
    public Groupe getGroupeById(Long id) {
        return groupeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe", "id", id));
    }
    
    /**
     * Récupérer un groupe par son code
     */
    @Transactional(readOnly = true)
    public Groupe getGroupeByCode(String code) {
        return groupeRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe", "code", code));
    }
    
    /**
     * Mettre à jour un groupe
     */
    public Groupe updateGroupe(Long id, Groupe groupeDetails) {
        Groupe groupe = getGroupeById(id);
        
        // Vérifier si le nouveau code n'existe pas déjà
        if (!groupe.getCode().equals(groupeDetails.getCode()) &&
            groupeRepository.existsByCode(groupeDetails.getCode())) {
            throw new BusinessException("Un groupe avec le code " + groupeDetails.getCode() + " existe déjà");
        }
        
        groupe.setCode(groupeDetails.getCode());
        groupe.setNom(groupeDetails.getNom());
        groupe.setCapaciteMax(groupeDetails.getCapaciteMax());
        
        return groupeRepository.save(groupe);
    }
    
    /**
     * Supprimer un groupe
     */
    public void deleteGroupe(Long id) {
        Groupe groupe = getGroupeById(id);
        groupeRepository.delete(groupe);
    }
    
    /**
     * Compter le nombre d'étudiants dans un groupe
     */
    @Transactional(readOnly = true)
    public Long countEtudiants(Long groupeId) {
        return groupeRepository.countEtudiantsByGroupeId(groupeId);
    }
}


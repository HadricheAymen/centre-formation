package spring.jpa.centre_formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.jpa.centre_formation.entity.Specialite;
import spring.jpa.centre_formation.exception.BusinessException;
import spring.jpa.centre_formation.exception.ResourceNotFoundException;
import spring.jpa.centre_formation.repository.SpecialiteRepository;

import java.util.List;

/**
 * Service métier pour la gestion des spécialités
 */
@Service
@Transactional
public class SpecialiteService {
    
    private final SpecialiteRepository specialiteRepository;
    
    @Autowired
    public SpecialiteService(SpecialiteRepository specialiteRepository) {
        this.specialiteRepository = specialiteRepository;
    }
    
    /**
     * Créer une nouvelle spécialité
     */
    public Specialite creerSpecialite(Specialite specialite) {
        // Vérifier si le code existe déjà
        if (specialiteRepository.existsByCode(specialite.getCode())) {
            throw new BusinessException("Une spécialité avec le code " + specialite.getCode() + " existe déjà");
        }
        
        return specialiteRepository.save(specialite);
    }
    
    /**
     * Récupérer toutes les spécialités
     */
    @Transactional(readOnly = true)
    public List<Specialite> getAllSpecialites() {
        return specialiteRepository.findAll();
    }
    
    /**
     * Récupérer une spécialité par son ID
     */
    @Transactional(readOnly = true)
    public Specialite getSpecialiteById(Long id) {
        return specialiteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialite", "id", id));
    }
    
    /**
     * Récupérer une spécialité par son code
     */
    @Transactional(readOnly = true)
    public Specialite getSpecialiteByCode(String code) {
        return specialiteRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Specialite", "code", code));
    }
    
    /**
     * Mettre à jour une spécialité
     */
    public Specialite updateSpecialite(Long id, Specialite specialiteDetails) {
        Specialite specialite = getSpecialiteById(id);
        
        // Vérifier si le nouveau code n'existe pas déjà
        if (!specialite.getCode().equals(specialiteDetails.getCode()) &&
            specialiteRepository.existsByCode(specialiteDetails.getCode())) {
            throw new BusinessException("Une spécialité avec le code " + specialiteDetails.getCode() + " existe déjà");
        }
        
        specialite.setCode(specialiteDetails.getCode());
        specialite.setNom(specialiteDetails.getNom());
        specialite.setDescription(specialiteDetails.getDescription());
        
        return specialiteRepository.save(specialite);
    }
    
    /**
     * Supprimer une spécialité
     */
    public void deleteSpecialite(Long id) {
        Specialite specialite = getSpecialiteById(id);
        specialiteRepository.delete(specialite);
    }
}


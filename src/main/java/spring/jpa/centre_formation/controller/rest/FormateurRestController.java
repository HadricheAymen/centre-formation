package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.jpa.centre_formation.entity.Formateur;
import spring.jpa.centre_formation.service.FormateurService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des formateurs
 */
@RestController
@RequestMapping("/api/formateurs")
@CrossOrigin(origins = "*")
public class FormateurRestController {
    
    private final FormateurService formateurService;
    
    @Autowired
    public FormateurRestController(FormateurService formateurService) {
        this.formateurService = formateurService;
    }
    
    /**
     * Récupérer tous les formateurs
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<List<Formateur>> getAllFormateurs() {
        List<Formateur> formateurs = formateurService.getAllFormateurs();
        return ResponseEntity.ok(formateurs);
    }
    
    /**
     * Récupérer un formateur par son ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<Formateur> getFormateurById(@PathVariable Long id) {
        Formateur formateur = formateurService.getFormateurById(id);
        return ResponseEntity.ok(formateur);
    }
    
    /**
     * Créer un nouveau formateur
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Formateur> creerFormateur(@RequestBody Formateur formateur) {
        Formateur nouveauFormateur = formateurService.creerFormateur(formateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauFormateur);
    }
    
    /**
     * Mettre à jour un formateur
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Formateur> updateFormateur(@PathVariable Long id, @RequestBody Formateur formateur) {
        Formateur formateurMisAJour = formateurService.updateFormateur(id, formateur);
        return ResponseEntity.ok(formateurMisAJour);
    }
    
    /**
     * Supprimer un formateur
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFormateur(@PathVariable Long id) {
        formateurService.deleteFormateur(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Rechercher des formateurs
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Formateur>> searchFormateurs(@RequestParam String keyword) {
        List<Formateur> formateurs = formateurService.searchFormateurs(keyword);
        return ResponseEntity.ok(formateurs);
    }
    
    /**
     * Récupérer les formateurs d'une spécialité
     */
    @GetMapping("/specialite/{specialiteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Formateur>> getFormateursBySpecialite(@PathVariable Long specialiteId) {
        List<Formateur> formateurs = formateurService.getFormateursBySpecialite(specialiteId);
        return ResponseEntity.ok(formateurs);
    }
    
    /**
     * Affecter une spécialité à un formateur
     */
    @PostMapping("/{formateurId}/specialite/{specialiteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Formateur> affecterSpecialite(@PathVariable Long formateurId, @PathVariable Long specialiteId) {
        Formateur formateur = formateurService.affecterSpecialite(formateurId, specialiteId);
        return ResponseEntity.ok(formateur);
    }
    
    /**
     * Compter le nombre de cours d'un formateur
     */
    @GetMapping("/{id}/cours/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Long> countCours(@PathVariable Long id) {
        Long count = formateurService.countCours(id);
        return ResponseEntity.ok(count);
    }
}


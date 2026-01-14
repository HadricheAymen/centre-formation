package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.jpa.centre_formation.entity.Inscription;
import spring.jpa.centre_formation.service.InscriptionService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des inscriptions
 */
@RestController
@RequestMapping("/api/inscriptions")
@CrossOrigin(origins = "*")
public class InscriptionRestController {
    
    private final InscriptionService inscriptionService;
    
    @Autowired
    public InscriptionRestController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }
    
    /**
     * Récupérer toutes les inscriptions
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Inscription>> getAllInscriptions() {
        List<Inscription> inscriptions = inscriptionService.getAllInscriptions();
        return ResponseEntity.ok(inscriptions);
    }
    
    /**
     * Récupérer une inscription par son ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable Long id) {
        Inscription inscription = inscriptionService.getInscriptionById(id);
        return ResponseEntity.ok(inscription);
    }
    
    /**
     * Inscrire un étudiant à un cours
     */
    @PostMapping("/etudiant/{etudiantId}/cours/{coursId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<Inscription> inscrireEtudiant(@PathVariable Long etudiantId, @PathVariable Long coursId) {
        Inscription inscription = inscriptionService.inscrireEtudiant(etudiantId, coursId);
        return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
    }
    
    /**
     * Récupérer les inscriptions d'un étudiant
     */
    @GetMapping("/etudiant/{etudiantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<List<Inscription>> getInscriptionsByEtudiant(@PathVariable Long etudiantId) {
        List<Inscription> inscriptions = inscriptionService.getInscriptionsByEtudiant(etudiantId);
        return ResponseEntity.ok(inscriptions);
    }
    
    /**
     * Récupérer les inscriptions actives d'un étudiant
     */
    @GetMapping("/etudiant/{etudiantId}/actives")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<List<Inscription>> getInscriptionsActivesByEtudiant(@PathVariable Long etudiantId) {
        List<Inscription> inscriptions = inscriptionService.getInscriptionsActivesByEtudiant(etudiantId);
        return ResponseEntity.ok(inscriptions);
    }
    
    /**
     * Récupérer les inscriptions d'un cours
     */
    @GetMapping("/cours/{coursId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Inscription>> getInscriptionsByCours(@PathVariable Long coursId) {
        List<Inscription> inscriptions = inscriptionService.getInscriptionsByCours(coursId);
        return ResponseEntity.ok(inscriptions);
    }
    
    /**
     * Récupérer les inscriptions actives d'un cours
     */
    @GetMapping("/cours/{coursId}/actives")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Inscription>> getInscriptionsActivesByCours(@PathVariable Long coursId) {
        List<Inscription> inscriptions = inscriptionService.getInscriptionsActivesByCours(coursId);
        return ResponseEntity.ok(inscriptions);
    }
    
    /**
     * Annuler une inscription
     */
    @PutMapping("/{id}/annuler")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<Inscription> annulerInscription(@PathVariable Long id) {
        Inscription inscription = inscriptionService.annulerInscription(id);
        return ResponseEntity.ok(inscription);
    }
    
    /**
     * Annuler l'inscription d'un étudiant à un cours
     */
    @PutMapping("/etudiant/{etudiantId}/cours/{coursId}/annuler")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<Inscription> annulerInscriptionEtudiantCours(
            @PathVariable Long etudiantId, @PathVariable Long coursId) {
        Inscription inscription = inscriptionService.annulerInscriptionEtudiantCours(etudiantId, coursId);
        return ResponseEntity.ok(inscription);
    }
    
    /**
     * Supprimer une inscription
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        inscriptionService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Compter les inscriptions actives d'un cours
     */
    @GetMapping("/cours/{coursId}/count")
    public ResponseEntity<Long> countInscriptionsActives(@PathVariable Long coursId) {
        Long count = inscriptionService.countInscriptionsActives(coursId);
        return ResponseEntity.ok(count);
    }
}


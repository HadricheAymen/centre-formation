package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.jpa.centre_formation.entity.Etudiant;
import spring.jpa.centre_formation.service.EtudiantService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des étudiants
 */
@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin(origins = "*")
public class EtudiantRestController {
    
    private final EtudiantService etudiantService;
    
    @Autowired
    public EtudiantRestController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }
    
    /**
     * Récupérer tous les étudiants
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }
    
    /**
     * Récupérer un étudiant par son ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        Etudiant etudiant = etudiantService.getEtudiantById(id);
        return ResponseEntity.ok(etudiant);
    }
    
    /**
     * Récupérer un étudiant par son matricule
     */
    @GetMapping("/matricule/{matricule}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Etudiant> getEtudiantByMatricule(@PathVariable String matricule) {
        Etudiant etudiant = etudiantService.getEtudiantByMatricule(matricule);
        return ResponseEntity.ok(etudiant);
    }
    
    /**
     * Créer un nouvel étudiant
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Etudiant> creerEtudiant(@RequestBody Etudiant etudiant) {
        Etudiant nouvelEtudiant = etudiantService.creerEtudiant(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelEtudiant);
    }
    
    /**
     * Mettre à jour un étudiant
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ETUDIANT')")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id, @RequestBody Etudiant etudiant) {
        Etudiant etudiantMisAJour = etudiantService.updateEtudiant(id, etudiant);
        return ResponseEntity.ok(etudiantMisAJour);
    }
    
    /**
     * Supprimer un étudiant
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Rechercher des étudiants
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Etudiant>> searchEtudiants(@RequestParam String keyword) {
        List<Etudiant> etudiants = etudiantService.searchEtudiants(keyword);
        return ResponseEntity.ok(etudiants);
    }
    
    /**
     * Récupérer les étudiants d'un groupe
     */
    @GetMapping("/groupe/{groupeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Etudiant>> getEtudiantsByGroupe(@PathVariable Long groupeId) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsByGroupe(groupeId);
        return ResponseEntity.ok(etudiants);
    }
    
    /**
     * Récupérer les étudiants d'un cours
     */
    @GetMapping("/cours/{coursId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Etudiant>> getEtudiantsByCours(@PathVariable Long coursId) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsByCours(coursId);
        return ResponseEntity.ok(etudiants);
    }
    
    /**
     * Affecter un étudiant à un groupe
     */
    @PostMapping("/{etudiantId}/groupes/{groupeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Etudiant> affecterAuGroupe(@PathVariable Long etudiantId, @PathVariable Long groupeId) {
        Etudiant etudiant = etudiantService.affecterAuGroupe(etudiantId, groupeId);
        return ResponseEntity.ok(etudiant);
    }
    
    /**
     * Retirer un étudiant d'un groupe
     */
    @DeleteMapping("/{etudiantId}/groupes/{groupeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Etudiant> retirerDuGroupe(@PathVariable Long etudiantId, @PathVariable Long groupeId) {
        Etudiant etudiant = etudiantService.retirerDuGroupe(etudiantId, groupeId);
        return ResponseEntity.ok(etudiant);
    }
    
    /**
     * Calculer la moyenne générale d'un étudiant
     */
    @GetMapping("/{id}/moyenne")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<Double> calculerMoyenneGenerale(@PathVariable Long id) {
        Double moyenne = etudiantService.calculerMoyenneGenerale(id);
        return ResponseEntity.ok(moyenne);
    }
}


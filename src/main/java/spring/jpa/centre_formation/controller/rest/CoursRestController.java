package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.jpa.centre_formation.entity.Cours;
import spring.jpa.centre_formation.service.CoursService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des cours
 */
@RestController
@RequestMapping("/api/cours")
@CrossOrigin(origins = "*")
public class CoursRestController {
    
    @Autowired
    private final CoursService coursService;
    
    
    public CoursRestController(CoursService coursService) {
        this.coursService = coursService;
    }
    
    /**
     * Récupérer tous les cours
     */
    @GetMapping
    public ResponseEntity<List<Cours>> getAllCours() {
        List<Cours> cours = coursService.getAllCours();
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Récupérer un cours par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cours> getCoursById(@PathVariable Long id) {
        Cours cours = coursService.getCoursById(id);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Récupérer un cours par son code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Cours> getCoursByCode(@PathVariable String code) {
        Cours cours = coursService.getCoursByCode(code);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Créer un nouveau cours
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cours> creerCours(@RequestBody Cours cours) {
        Cours nouveauCours = coursService.creerCours(cours);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauCours);
    }
    
    /**
     * Mettre à jour un cours
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Cours> updateCours(@PathVariable Long id, @RequestBody Cours cours) {
        Cours coursMisAJour = coursService.updateCours(id, cours);
        return ResponseEntity.ok(coursMisAJour);
    }
    
    /**
     * Supprimer un cours
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Rechercher des cours
     */
    @GetMapping("/search")
    public ResponseEntity<List<Cours>> searchCours(@RequestParam String keyword) {
        List<Cours> cours = coursService.searchCours(keyword);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Récupérer les cours d'un formateur
     */
    @GetMapping("/formateur/{formateurId}")
    public ResponseEntity<List<Cours>> getCoursByFormateur(@PathVariable Long formateurId) {
        List<Cours> cours = coursService.getCoursByFormateur(formateurId);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Récupérer les cours d'une spécialité
     */
    @GetMapping("/specialite/{specialiteId}")
    public ResponseEntity<List<Cours>> getCoursBySpecialite(@PathVariable Long specialiteId) {
        List<Cours> cours = coursService.getCoursBySpecialite(specialiteId);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Récupérer les cours d'une session
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Cours>> getCoursBySession(@PathVariable Long sessionId) {
        List<Cours> cours = coursService.getCoursBySession(sessionId);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Récupérer les cours d'un groupe
     */
    @GetMapping("/groupe/{groupeId}")
    public ResponseEntity<List<Cours>> getCoursByGroupe(@PathVariable Long groupeId) {
        List<Cours> cours = coursService.getCoursByGroupe(groupeId);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Affecter un formateur à un cours
     */
    @PostMapping("/{coursId}/formateur/{formateurId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cours> affecterFormateur(@PathVariable Long coursId, @PathVariable Long formateurId) {
        Cours cours = coursService.affecterFormateur(coursId, formateurId);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Affecter une spécialité à un cours
     */
    @PostMapping("/{coursId}/specialite/{specialiteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cours> affecterSpecialite(@PathVariable Long coursId, @PathVariable Long specialiteId) {
        Cours cours = coursService.affecterSpecialite(coursId, specialiteId);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Affecter une session à un cours
     */
    @PostMapping("/{coursId}/session/{sessionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cours> affecterSession(@PathVariable Long coursId, @PathVariable Long sessionId) {
        Cours cours = coursService.affecterSession(coursId, sessionId);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Affecter un groupe à un cours
     */
    @PostMapping("/{coursId}/groupes/{groupeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cours> affecterGroupe(@PathVariable Long coursId, @PathVariable Long groupeId) {
        Cours cours = coursService.affecterGroupe(coursId, groupeId);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Retirer un groupe d'un cours
     */
    @DeleteMapping("/{coursId}/groupes/{groupeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cours> retirerGroupe(@PathVariable Long coursId, @PathVariable Long groupeId) {
        Cours cours = coursService.retirerGroupe(coursId, groupeId);
        return ResponseEntity.ok(cours);
    }
    
    /**
     * Compter le nombre d'inscriptions à un cours
     */
    @GetMapping("/{id}/inscriptions/count")
    public ResponseEntity<Long> countInscriptions(@PathVariable Long id) {
        Long count = coursService.countInscriptions(id);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Calculer la moyenne d'un cours
     */
    @GetMapping("/{id}/moyenne")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Double> calculerMoyenneCours(@PathVariable Long id) {
        Double moyenne = coursService.calculerMoyenneCours(id);
        return ResponseEntity.ok(moyenne);
    }
    
    /**
     * Calculer le taux de réussite d'un cours
     */
    @GetMapping("/{id}/taux-reussite")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Double> calculerTauxReussite(@PathVariable Long id) {
        Double taux = coursService.calculerTauxReussite(id);
        return ResponseEntity.ok(taux);
    }
}


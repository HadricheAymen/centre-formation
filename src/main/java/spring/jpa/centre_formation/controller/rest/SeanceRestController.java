package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.jpa.centre_formation.entity.Seance;
import spring.jpa.centre_formation.service.SeanceService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des séances
 */
@RestController
@RequestMapping("/api/seances")
@CrossOrigin(origins = "*")
public class SeanceRestController {
    
    private final SeanceService seanceService;
    
    @Autowired
    public SeanceRestController(SeanceService seanceService) {
        this.seanceService = seanceService;
    }
    
    @GetMapping
    public ResponseEntity<List<Seance>> getAllSeances() {
        List<Seance> seances = seanceService.getAllSeances();
        return ResponseEntity.ok(seances);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Seance> getSeanceById(@PathVariable Long id) {
        Seance seance = seanceService.getSeanceById(id);
        return ResponseEntity.ok(seance);
    }
    
    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Seance>> getSeancesByCours(@PathVariable Long coursId) {
        List<Seance> seances = seanceService.getSeancesByCours(coursId);
        return ResponseEntity.ok(seances);
    }
    
    @GetMapping("/cours/{coursId}/actives")
    public ResponseEntity<List<Seance>> getSeancesActivesByCours(@PathVariable Long coursId) {
        List<Seance> seances = seanceService.getSeancesActivesByCours(coursId);
        return ResponseEntity.ok(seances);
    }
    
    @GetMapping("/salle/{salle}")
    public ResponseEntity<List<Seance>> getSeancesBySalle(@PathVariable String salle) {
        List<Seance> seances = seanceService.getSeancesBySalle(salle);
        return ResponseEntity.ok(seances);
    }
    
    @GetMapping("/periode")
    public ResponseEntity<List<Seance>> getSeancesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        List<Seance> seances = seanceService.getSeancesByPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(seances);
    }
    
    @GetMapping("/emploi-du-temps/formateur/{formateurId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<List<Seance>> getEmploiDuTempsFormateur(
            @PathVariable Long formateurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        List<Seance> seances = seanceService.getEmploiDuTempsFormateur(formateurId, dateDebut, dateFin);
        return ResponseEntity.ok(seances);
    }
    
    @GetMapping("/emploi-du-temps/etudiant/{etudiantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR', 'ETUDIANT')")
    public ResponseEntity<List<Seance>> getEmploiDuTempsEtudiant(
            @PathVariable Long etudiantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        List<Seance> seances = seanceService.getEmploiDuTempsEtudiant(etudiantId, dateDebut, dateFin);
        return ResponseEntity.ok(seances);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Seance> creerSeance(@RequestBody Seance seance) {
        Seance nouvelleSeance = seanceService.creerSeance(seance);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleSeance);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Seance> updateSeance(@PathVariable Long id, @RequestBody Seance seance) {
        Seance seanceMiseAJour = seanceService.updateSeance(id, seance);
        return ResponseEntity.ok(seanceMiseAJour);
    }
    
    @PutMapping("/{id}/annuler")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Seance> annulerSeance(@PathVariable Long id) {
        Seance seance = seanceService.annulerSeance(id);
        return ResponseEntity.ok(seance);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        seanceService.deleteSeance(id);
        return ResponseEntity.noContent().build();
    }
}


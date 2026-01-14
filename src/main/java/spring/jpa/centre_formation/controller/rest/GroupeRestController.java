package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.jpa.centre_formation.entity.Groupe;
import spring.jpa.centre_formation.service.GroupeService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des groupes
 */
@RestController
@RequestMapping("/api/groupes")
@CrossOrigin(origins = "*")
public class GroupeRestController {
    
    private final GroupeService groupeService;
    
    @Autowired
    public GroupeRestController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }
    
    @GetMapping
    public ResponseEntity<List<Groupe>> getAllGroupes() {
        List<Groupe> groupes = groupeService.getAllGroupes();
        return ResponseEntity.ok(groupes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Groupe> getGroupeById(@PathVariable Long id) {
        Groupe groupe = groupeService.getGroupeById(id);
        return ResponseEntity.ok(groupe);
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Groupe> getGroupeByCode(@PathVariable String code) {
        Groupe groupe = groupeService.getGroupeByCode(code);
        return ResponseEntity.ok(groupe);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Groupe> creerGroupe(@RequestBody Groupe groupe) {
        Groupe nouveauGroupe = groupeService.creerGroupe(groupe);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauGroupe);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Groupe> updateGroupe(@PathVariable Long id, @RequestBody Groupe groupe) {
        Groupe groupeMisAJour = groupeService.updateGroupe(id, groupe);
        return ResponseEntity.ok(groupeMisAJour);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGroupe(@PathVariable Long id) {
        groupeService.deleteGroupe(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/etudiants/count")
    public ResponseEntity<Long> countEtudiants(@PathVariable Long id) {
        Long count = groupeService.countEtudiants(id);
        return ResponseEntity.ok(count);
    }
}


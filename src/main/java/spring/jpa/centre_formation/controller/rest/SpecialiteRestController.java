package spring.jpa.centre_formation.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.jpa.centre_formation.entity.Specialite;
import spring.jpa.centre_formation.service.SpecialiteService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des spécialités
 */
@RestController
@RequestMapping("/api/specialites")
@CrossOrigin(origins = "*")
public class SpecialiteRestController {
    
    private final SpecialiteService specialiteService;
    
    @Autowired
    public SpecialiteRestController(SpecialiteService specialiteService) {
        this.specialiteService = specialiteService;
    }
    
    @GetMapping
    public ResponseEntity<List<Specialite>> getAllSpecialites() {
        List<Specialite> specialites = specialiteService.getAllSpecialites();
        return ResponseEntity.ok(specialites);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Specialite> getSpecialiteById(@PathVariable Long id) {
        Specialite specialite = specialiteService.getSpecialiteById(id);
        return ResponseEntity.ok(specialite);
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Specialite> getSpecialiteByCode(@PathVariable String code) {
        Specialite specialite = specialiteService.getSpecialiteByCode(code);
        return ResponseEntity.ok(specialite);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Specialite> creerSpecialite(@RequestBody Specialite specialite) {
        Specialite nouvelleSpecialite = specialiteService.creerSpecialite(specialite);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleSpecialite);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Specialite> updateSpecialite(@PathVariable Long id, @RequestBody Specialite specialite) {
        Specialite specialiteMiseAJour = specialiteService.updateSpecialite(id, specialite);
        return ResponseEntity.ok(specialiteMiseAJour);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSpecialite(@PathVariable Long id) {
        specialiteService.deleteSpecialite(id);
        return ResponseEntity.noContent().build();
    }
}

